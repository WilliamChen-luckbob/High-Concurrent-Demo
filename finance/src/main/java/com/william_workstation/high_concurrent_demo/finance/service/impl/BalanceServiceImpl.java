package com.william_workstation.high_concurrent_demo.finance.service.impl;

import com.william_workstation.high_concurrent_demo.finance.entity.Balance;
import com.william_workstation.high_concurrent_demo.finance.mapper.BalanceMapper;
import com.william_workstation.high_concurrent_demo.finance.service.IBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author william
 * @since 2020-11-05
 */
@Service
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements IBalanceService {

}
