# rich-easymoney
东方财富，量化交易，自动交易

# 功能说明
## 一.已完成
### 1.基础功能
    web请求处理，单测模块
### 2.http请求模块 
    请求东财get，post请求

### 年后的行情太差了，等行情走出来再看

## 二.待实现
### 1.东财对接
    登陆，自选股列表，持仓列表及盈亏，买入，卖出，撤单
    
### 2.技术指标数据获取
    日线macd，kdj技术指标数据获取加工
    日内macd数据获取，金叉死叉判断
    历史收盘价
    
### 3.核心策略
    W底判断，M顶判断
    技术指标的金叉死叉判断
    三只乌鸦等特殊形态判断
    区间震荡和箱体判断
    PE，PEG等数据判断
    
### 3.基础功能
    jdbc整合，redis整合
    
### 0220
    macd，kdj数据计算以及金叉判断有点复杂，先实现一套简单的策略（新高策略），
    对某一只票，保证几手底仓不动，跌多少后，加一手，反弹多少后，把之前加的出掉做T。新高后，把底仓之外的清掉。
    标的：东方雨虹，未来3-5年年均30%的符合增长率，防水行业龙头，行业天花板有望突破。
