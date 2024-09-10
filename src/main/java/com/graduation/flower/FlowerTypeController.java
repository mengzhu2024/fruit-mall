package com.graduation.flower;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.graduation.common.ObjectTransfer;
import com.graduation.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flower-type")
public class FlowerTypeController {

    @Resource
    private FlowerTypeMapper flowerTypeMapper;

    @ResponseBody
    @PostMapping("/query")
    public Result<List<FlowerTypeModel>> query(@RequestBody FlowerTypeQuery query) {
        return new Result<>(flowerTypeMapper.selectJoinList(FlowerTypeDO.class, getQueryWrapper(query)).stream()
                .map(e -> ObjectTransfer.transfer(e, FlowerTypeModel.class))
                .collect(Collectors.toList()));
    }

    @ResponseBody
    @PostMapping("/page")
    public Result<IPage<FlowerTypeModel>> page(@RequestBody FlowerTypeQuery query) {
        IPage<FlowerTypeDO> page = new Page<>(query.getCurrent(), 10);
        flowerTypeMapper.selectJoinPage(page, FlowerTypeDO.class, getQueryWrapper(query));
        IPage<FlowerTypeModel> result = new Page<>();
        result.setPages((int) page.getPages());
        result.setCurrent((int) Math.min(page.getCurrent(), page.getPages()));
        result.setTotal((int) page.getTotal());
        result.setRecords(page.getRecords().stream().map(e -> {
            FlowerTypeModel model = ObjectTransfer.transfer(e, FlowerTypeModel.class);
            return model;
        }).collect(Collectors.toList()));
        return new Result<>(result);
    }

    @ResponseBody
    @PostMapping("/add")
    public Result<?> add(@RequestBody FlowerTypeModel model) {
        FlowerTypeDO flowerTypeDO = ObjectTransfer.transfer(model, FlowerTypeDO.class);
        flowerTypeMapper.insert(flowerTypeDO);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    public Result<?> update(@RequestBody FlowerTypeModel model) {
        FlowerTypeDO flowerTypeDO = ObjectTransfer.transfer(model, FlowerTypeDO.class);
        flowerTypeMapper.updateById(flowerTypeDO);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/delete")
    public Result<?> delete(Integer id) {
        flowerTypeMapper.deleteById(id);
        return Result.success();
    }

    private MPJLambdaWrapper<FlowerTypeDO> getQueryWrapper(FlowerTypeQuery query) {
        return new MPJLambdaWrapper<FlowerTypeDO>()
                .selectAll(FlowerTypeDO.class)
                .eq(Objects.nonNull(query.getId()), FlowerTypeDO::getId, query.getId())
                .like(StrUtil.isNotBlank(query.getName()), FlowerTypeDO::getName, query.getName())
                ;
    }
}
