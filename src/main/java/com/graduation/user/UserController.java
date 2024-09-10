package com.graduation.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.graduation.common.ObjectTransfer;
import com.graduation.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @ResponseBody
    @PostMapping("/query")
    public Result<List<UserModel>> query(@RequestBody UserQuery query) {
        return new Result<>(userMapper.selectJoinList(UserDO.class, getQueryWrapper(query)).stream()
                .map(e -> ObjectTransfer.transfer(e, UserModel.class))
                .collect(Collectors.toList()));
    }

    @ResponseBody
    @PostMapping("/page")
    public Result<IPage<UserModel>> page(@RequestBody UserQuery query) {
        IPage<UserDO> page = new Page<>(query.getCurrent(), 10);
        userMapper.selectJoinPage(page, UserDO.class, getQueryWrapper(query));
        IPage<UserModel> result = new Page<>();
        result.setPages((int) page.getPages());
        result.setCurrent((int) Math.min(page.getCurrent(), page.getPages()));
        result.setTotal((int) page.getTotal());
        result.setRecords(page.getRecords().stream().map(e -> {
            UserModel model = ObjectTransfer.transfer(e, UserModel.class);
            return model;
        }).collect(Collectors.toList()));
        return new Result<>(result);
    }

    @ResponseBody
    @PostMapping("/add")
    public Result<?> add(@RequestBody UserModel model) {
        if (userMapper.selectCount(new LambdaQueryWrapper<UserDO>().eq(UserDO::getAccount, model.getAccount())) > 0) {
            return Result.failed("该账号已注册，请修改");
        }
        UserDO userDO = ObjectTransfer.transfer(model, UserDO.class);
        userDO.setCreateTime(LocalDateTime.now());
        userMapper.insert(userDO);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    public Result<?> update(@RequestBody UserModel model) {
        UserDO userDO = ObjectTransfer.transfer(model, UserDO.class);
        userMapper.updateById(userDO);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/delete")
    public Result<?> delete(Integer id) {
        userMapper.deleteById(id);
        return Result.success();
    }

    private MPJLambdaWrapper<UserDO> getQueryWrapper(UserQuery query) {
        return new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .eq(Objects.nonNull(query.getId()), UserDO::getId, query.getId())
                .like(StrUtil.isNotBlank(query.getName()), UserDO::getName, query.getName())
                .like(StrUtil.isNotBlank(query.getSex()), UserDO::getSex, query.getSex())
                .like(StrUtil.isNotBlank(query.getAccount()), UserDO::getAccount, query.getAccount())
                .like(StrUtil.isNotBlank(query.getPassword()), UserDO::getPassword, query.getPassword())
                .like(StrUtil.isNotBlank(query.getPhone()), UserDO::getPhone, query.getPhone())
                .like(StrUtil.isNotBlank(query.getRole()), UserDO::getRole, query.getRole())
                ;
    }
}
