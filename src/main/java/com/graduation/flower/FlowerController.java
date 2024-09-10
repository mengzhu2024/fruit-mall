package com.graduation.flower;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.graduation.collect.UserCollectDO;
import com.graduation.collect.UserCollectMapper;
import com.graduation.common.LoginUtil;
import com.graduation.common.ObjectTransfer;
import com.graduation.common.Result;
import com.graduation.user.UserDO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flower")
public class FlowerController {

    @Resource
    private FlowerMapper flowerMapper;
    @Resource
    private FlowerTypeMapper flowerTypeMapper;
    @Resource
    private UserCollectMapper userCollectMapper;

    @ResponseBody
    @PostMapping("/query")
    public Result<List<FlowerModel>> query(HttpServletRequest request, @RequestBody FlowerQuery query) {
        UserDO loginer = LoginUtil.loginer(request);
        List<Integer> userCollectList = new ArrayList<>();
        if (Objects.nonNull(loginer)) {
            userCollectList.addAll(userCollectMapper.selectList(new LambdaQueryWrapper<UserCollectDO>()
                            .eq(UserCollectDO::getUserId, loginer.getId()))
                    .stream().map(UserCollectDO::getFlowerId)
                    .collect(Collectors.toList()));
        }
        if (query.isOnlyCollect()) {
            if (CollectionUtil.isEmpty(userCollectList)) {
                return new Result<>(new ArrayList<>());
            }
            query.setIdList(userCollectList);
        }
        return new Result<>(flowerMapper.selectJoinList(FlowerDO.class, getQueryWrapper(query)).stream()
                .map(e -> {
                    FlowerModel model = ObjectTransfer.transfer(e, FlowerModel.class);
                    if (userCollectList.contains(model.getId())) {
                        model.setIfCollect(true);
                    }
                    return model;
                })
                .collect(Collectors.toList()));
    }

    @ResponseBody
    @PostMapping("/page")
    public Result<IPage<FlowerModel>> page(@RequestBody FlowerQuery query) {
        IPage<FlowerDO> page = new Page<>(query.getCurrent(), 10);
        flowerMapper.selectJoinPage(page, FlowerDO.class, getQueryWrapper(query));
        IPage<FlowerModel> result = new Page<>();
        result.setPages((int) page.getPages());
        result.setCurrent((int) Math.min(page.getCurrent(), page.getPages()));
        result.setTotal((int) page.getTotal());
        Map<Integer, FlowerTypeModel> flowerTypeMap = flowerTypeMapper.selectList(new LambdaQueryWrapper<>()).stream()
                .map(e -> ObjectTransfer.transfer(e, FlowerTypeModel.class))
                .collect(Collectors.toMap(FlowerTypeModel::getId, Function.identity()));
        result.setRecords(page.getRecords().stream().map(e -> {
            FlowerModel model = ObjectTransfer.transfer(e, FlowerModel.class);
            model.setType(flowerTypeMap.get(e.getTypeId()));
            return model;
        }).collect(Collectors.toList()));
        return new Result<>(result);
    }

    @ResponseBody
    @PostMapping("/add")
    public Result<?> add(@RequestBody FlowerModel model) {
        FlowerDO flowerDO = ObjectTransfer.transfer(model, FlowerDO.class);
        flowerDO.setCreateTime(LocalDateTime.now());
        flowerMapper.insert(flowerDO);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    public Result<?> update(@RequestBody FlowerModel model) {
        FlowerDO flowerDO = ObjectTransfer.transfer(model, FlowerDO.class);
        flowerMapper.updateById(flowerDO);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/delete")
    public Result<?> delete(Integer id) {
        flowerMapper.deleteById(id);
        return Result.success();
    }

    private MPJLambdaWrapper<FlowerDO> getQueryWrapper(FlowerQuery query) {
        return new MPJLambdaWrapper<FlowerDO>()
                .selectAll(FlowerDO.class)
                .eq(Objects.nonNull(query.getId()), FlowerDO::getId, query.getId())
                .like(StrUtil.isNotBlank(query.getName()), FlowerDO::getName, query.getName())
                .eq(Objects.nonNull(query.getTypeId()), FlowerDO::getTypeId, query.getTypeId())
                .like(StrUtil.isNotBlank(query.getImgUrl()), FlowerDO::getImgUrl, query.getImgUrl())
                .like(StrUtil.isNotBlank(query.getStatus()), FlowerDO::getStatus, query.getStatus())
                .eq(Objects.nonNull(query.getStoreNum()), FlowerDO::getStoreNum, query.getStoreNum())
                .in(query.isOnlyCollect(), FlowerDO::getId, query.getIdList())
                ;
    }
}
