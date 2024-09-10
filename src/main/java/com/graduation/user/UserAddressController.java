package com.graduation.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.graduation.common.LoginUtil;
import com.graduation.common.ObjectTransfer;
import com.graduation.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-address")
public class UserAddressController {

    @Resource
    private UserAddressMapper userAddressMapper;
    @Resource
    private UserMapper userMapper;

    @ResponseBody
    @PostMapping("/query")
    public Result<List<UserAddressModel>> query(HttpServletRequest request, @RequestBody UserAddressQuery query) {
        UserDO loginer = LoginUtil.loginer(request);
        if (Objects.isNull(loginer)) {
            return new Result<>(new ArrayList<>());
        }
        query.setUserId(loginer.getId());
        return new Result<>(userAddressMapper.selectJoinList(UserAddressDO.class, getQueryWrapper(query)).stream()
                .map(e -> ObjectTransfer.transfer(e, UserAddressModel.class))
                .collect(Collectors.toList()));
    }

    @ResponseBody
    @PostMapping("/page")
    public Result<IPage<UserAddressModel>> page(HttpServletRequest request, @RequestBody UserAddressQuery query) {
        query.setUserId(LoginUtil.loginer(request).getId());
        IPage<UserAddressDO> page = new Page<>(query.getCurrent(), 10);
        userAddressMapper.selectJoinPage(page, UserAddressDO.class, getQueryWrapper(query));
        IPage<UserAddressModel> result = new Page<>();
        result.setPages((int) page.getPages());
        result.setCurrent((int) Math.min(page.getCurrent(), page.getPages()));
        result.setTotal((int) page.getTotal());
        Map<Integer, UserModel> userMap = userMapper.selectList(new LambdaQueryWrapper<>()).stream()
                .map(e -> ObjectTransfer.transfer(e, UserModel.class))
                .collect(Collectors.toMap(UserModel::getId, Function.identity()));
        result.setRecords(page.getRecords().stream().map(e -> {
            UserAddressModel model = ObjectTransfer.transfer(e, UserAddressModel.class);
            model.setUser(userMap.get(e.getUserId()));
            return model;
        }).collect(Collectors.toList()));
        return new Result<>(result);
    }

    @ResponseBody
    @PostMapping("/add")
    public Result<?> add(HttpServletRequest request, @RequestBody UserAddressModel model) {
        UserAddressDO userAddressDO = ObjectTransfer.transfer(model, UserAddressDO.class);
        userAddressDO.setUserId(LoginUtil.loginer(request).getId());
        userAddressMapper.insert(userAddressDO);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    public Result<?> update(@RequestBody UserAddressModel model) {
        UserAddressDO userAddressDO = ObjectTransfer.transfer(model, UserAddressDO.class);
        userAddressMapper.updateById(userAddressDO);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/delete")
    public Result<?> delete(Integer id) {
        userAddressMapper.deleteById(id);
        return Result.success();
    }

    private MPJLambdaWrapper<UserAddressDO> getQueryWrapper(UserAddressQuery query) {
        return new MPJLambdaWrapper<UserAddressDO>()
                .selectAll(UserAddressDO.class)
                .eq(Objects.nonNull(query.getId()), UserAddressDO::getId, query.getId())
                .eq(Objects.nonNull(query.getUserId()), UserAddressDO::getUserId, query.getUserId())
                .like(StrUtil.isNotBlank(query.getName()), UserAddressDO::getName, query.getName())
                .like(StrUtil.isNotBlank(query.getTelnum()), UserAddressDO::getTelnum, query.getTelnum())
                .like(StrUtil.isNotBlank(query.getAddress()), UserAddressDO::getAddress, query.getAddress())
                ;
    }
}
