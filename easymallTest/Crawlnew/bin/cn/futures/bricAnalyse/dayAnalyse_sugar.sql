/*-- 用于白糖首页展示数据的计算*/
DELIMITER $$

CREATE procedure `bricdata`.`dayAnalyse_sugar`()
  BEGIN
-- 声明时间相关变量 最终得到@todayNowHour时间格式为当前时间 eg @todayNowHour:2015020914;
  --  @todayStartHour:2015020900
DECLARE today INT;
DECLARE todaystr,todayNowHour,todayStartHour VARCHAR(20);
DECLARE year,month,day,hour INT;

-- 声明相关参数变量 并初始化参数的值
DECLARE funCostForeign,storeCostForeign,fundCostInternal,storeCostInternal FLOAT;
DECLARE daysPerYear INT;
DECLARE shipTime,processingCost,exchangeRateDollar,sugarProcLossTailand,
sugarProcLossBrazil,
tariffAdditional,
tariffNormal,
addedValueTax,
interestAndLaborFee,
FeesOfBanks,
TradeAgentCost,
InsuranceCost,
xuanguangduRaise,
PtPerT,
KgPerPt,
q8, -- 运费
q9,
q10,
futures_margin_per, -- 国内资金成本按年化8%计，期货保证金按8%计算；
futures_margin_per_for_Internal FLOAT;-- 国际资金成本按年化4%计，期货保证金按20%计算

-- 声明变量  用作对表'CX_MarketData_DayAnalyse_Sugar'进行insert操作
-- a*:监测类型, b*:标的1, c*:价格1, d*:标的2, e*:价格2, f*:价差, g*:变化, h*:持有时间, i*:资金成本, j*:仓储成本, k*:损耗/加工, l*:其他, m*:总计, n*:交割时间1, o*:交割时间2.
DECLARE a10 ,a9, a3 ,a4 ,a5 ,a6 ,a7 ,a8 VARCHAR(20);
DECLARE b10 ,b9 ,b3 ,b4 ,b5 ,b6 ,b7 ,b8 VARCHAR(20);
DECLARE c10 ,c9 ,c3 ,c4 ,c5 ,c6 ,c7 ,c8 FLOAT;
DECLARE d10 ,d9 ,d3 ,d4 ,d5 ,d6 ,d7 ,d8 VARCHAR(20);
DECLARE e10 ,e9 ,e3 ,e4 ,e5 ,e6 ,e7 ,e8 FLOAT;
DECLARE f10 ,f9 ,f3 ,f4 ,f5 ,f6 ,f7 ,f8 FLOAT;
DECLARE g10 ,g9 ,g3 ,g4 ,g5 ,g6 ,g7 ,g8 FLOAT;
DECLARE h10 ,h9 ,h3 ,h4 ,h5 ,h6 ,h7 ,h8 FLOAT;
DECLARE i10 ,i9 ,i3 ,i4 ,i5 ,i6 ,i7 ,i8 FLOAT;
DECLARE j10 ,j9 ,j3 ,j4 ,j5 ,j6 ,j7 ,j8 FLOAT;
DECLARE k10 ,k9 ,k3 ,k4 ,k5 ,k6 ,k7 ,k8 FLOAT;
DECLARE l10 ,l9 ,l3 ,l4 ,l5 ,l6 ,l7 ,l8 FLOAT;
DECLARE m10 ,m9 ,m3 ,m4 ,m5 ,m6 ,m7 ,m8 FLOAT;
DECLARE n10 ,n9 ,n3 ,n4 ,n5 ,n6 ,n7 ,n8 DATETIME;
DECLARE o10 ,o9 ,o3 ,o4 ,o5 ,o6 ,o7 ,o8 DATETIME;

-- 计算几个需要取期货数据的地方从哪个合约取数据
DECLARE srcode_b5 ,srcode_b6 ,srcode_d6 VARCHAR(200);

-- 拼写sql计算价格数据
DECLARE v_sql VARCHAR(1000);

-- 看看ice数据需要取哪个,ice合约月份为1，3,5,7,10,交割日期为合约月份前一个月的月底，切换时间也是。1月份数据很少，所以基本不会看
 -- 10.1至2.28  看3月合约。  3456月看下一号合约，789看10月合约
DECLARE icecode1,icecode2 VARCHAR(20);
DECLARE icemonthtmp1,icemonthtmp2 INT;

DECLARE code_date VARCHAR(20);
DECLARE my_year,my_month INT;-- 工具变量

-- 巴西和泰国现货价格
DECLARE shengtieshuiBaxi,shengtieshuiTaiguo FLOAT;

-- 计算变化
DECLARE jiachaLast FLOAT;

-- -FOB(离岸价) ＝（原糖价+升水）×重量单位换算值×（1+旋光度增值）。
-- -原糖到岸价（税后）=((FOB+运费）*（1+保险费率）*汇率*（1+贸易代理费率）*（1+银行手续费率）+利息及劳务费）*（1+关税）*（1+增值税）
-- -加工/损耗=原糖到岸价*（1+原糖加工损耗）+加工费
DECLARE FOB8,FOB9,FOB10 FLOAT;
DECLARE daoanjia8,daoanjia9,daoanjia10 FLOAT;



SET year = YEAR(NOW()),month=MONTH(NOW()),day=DAY(NOW()),hour=DATE_FORMAT(now(),'%H');
SET today=year*10000+month*100+day;
SET todaystr = CONCAT(today,'');
SET todayNowHour = CONCAT((today*100+hour),'');
SET todayStartHour = CONCAT(todaystr,'00');




SET funCostForeign=0.04,-- 国际资金成本按年化4%计
storeCostForeign=0.03,-- 仓储成本：国外按0.03美元/吨·天计
fundCostInternal=0.08,-- 国内资金成本按年化8%计
-- 2015.2.10公式改动 加入了新的参数  ‘@futures_margin_per’ ‘@futures_margin_per_for_Internal’
futures_margin_per = 0.08,-- 国内资金成本按年化8%计，期货保证金按8%计算； 丁鹏
futures_margin_per_for_Internal = 0.2,-- 国际资金成本按年化4%计，期货保证金按20%计算
storeCostInternal=0.45,-- 仓储成本：国内按0.45元/吨·天计
shipTime=45,
daysPerYear=360,
processingCost=400,  -- 加工费
exchangeRateDollar=6.15,
sugarProcLossTailand=0.04,
sugarProcLossBrazil=0.035,
tariffAdditional=0.5,-- 配额外关税
tariffNormal=0.15,   -- 配额内关税
addedValueTax=0.17,  -- 增值税
interestAndLaborFee=25,-- 利息及劳务费
FeesOfBanks=0.00125,-- 银行手续费率
TradeAgentCost=0.003,-- 贸易代理费率
InsuranceCost=0.004616,-- 保险费率
xuanguangduRaise=0.0275,-- 旋光度增值
PtPerT=22.0462,   -- 重量转换比例
KgPerPt=0.454,     -- 重量转换比例
q8=42,
q9 = 25,
q10 =33.5;



SET a3='期现套利',a4='期现套利',a5='跨期套利',a6='跨期套利',a7='跨期套利',a8='跨市套利',a9='跨市套利',a10='跨市套利';
SET b3='广西现货',b4='广西现货',b8='巴西现货',b9='泰国现货';
SET n3=NOW(),n4=NOW(),n8=NOW(),n9=NOW();
SET k3=10, k4=30, k5=20, k6=20, k7=0; -- 为参数‘损耗/加工’赋值
SET l3=20, l4=20, l5=20, l6=20, l7=0.2; -- 为参数‘其他’赋值


-- 以上是需要用到的变量的定义，以下开始计算表
-- 广西现货数据
SELECT 广西 INTO c3 FROM CX_Price WHERE CX_Price.varId=4 AND timeint<=today ORDER BY timeint DESC,edittime DESC LIMIT 1;
SET c4=c3;
-- 计算几个需要取期货数据的地方从哪个合约取数据
IF (today%10000>1215 or today%10000<=415 )
    THEN -- 计算交割时间
    SET srcode_b5 = 'CX_MarketData_5FH';
    SET srcode_b6 = 'CX_MarketData_7FH';
    SET srcode_d6 = 'CX_MarketData_3FH';
    IF(today%10000>1215)
        THEN
        SET b5=CONCAT('SR',(year%10+1),'05');
        SET n5=CAST(((year+1)*10000+5*100+15) AS DATETIME);
        SET b6=CONCAT('SR',(year%10+1),'09');
        SET n6=CAST(((year+1)*10000+9*100+15) AS DATETIME);
        SET d6=CONCAT('SR',(year%10+2),'01');
        SET o6=CAST(((year+2)*10000+1*100+15) AS DATETIME);
        -- set @n5=
    ELSE
        SET b5=CONCAT('SR',(year%10),'05');
        SET n5=CAST(((year)*10000+5*100+15) AS DATETIME);
        SET b6=CONCAT('SR',(year%10),'09');
        SET n6=CAST(((year)*10000+9*100+15) AS DATETIME);
        SET d6=CONCAT('SR',(year%10+1),'01');
        SET o6=CAST(((year+1)*10000+1*100+15) AS DATETIME);
    END IF;
ELSEIF(today%10000>415 and today%10000<=815 )
    THEN
    set srcode_b5 = 'CX_MarketData_7FH';
    set srcode_b6 = 'CX_MarketData_3FH';
    set srcode_d6 = 'CX_MarketData_5FH';
    set b5=CONCAT('SR',(year%10),'09');
    set n5=CAST(((year)*10000+9*100+15) AS DATETIME);
    set b6=CONCAT('SR',(year%10+1),'01');
    set n6=CAST(((year+1)*10000+1*100+15) AS DATETIME);
    set d6=CONCAT('SR',(year%10+1),'05');
    set o6=CAST(((year+1)*10000+5*100+15) AS DATETIME);
ELSE
    set srcode_b5 = 'CX_MarketData_3FH';
    set srcode_b6 = 'CX_MarketData_5FH';
    set srcode_d6 = 'CX_MarketData_7FH';
    set b5=CONCAT('SR',(year%10+1),'01');
    set n5=CAST(((year+1)*10000+1*100+15) AS DATETIME);
    set b6=CONCAT('SR',(year%10+1),'05');
    set n6=CAST(((year+1)*10000+5*100+15) AS DATETIME);
    set d6=CONCAT('SR',(year%10+1),'09');
    set o6=CAST(((year+1)*10000+9*100+15) AS DATETIME);
END IF;
-- 拼写sql计算价格数据
SET @v_sql=CONCAT('SELECT 收盘价 INTO @c5 FROM ',srcode_b5,' WHERE ',srcode_b5,'.varId =139 AND timeint<= '
         ,todayNowHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    prepare stmt from @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    deallocate prepare stmt;     -- 释放掉预处理段
    SET c5=@c5;
SET @v_sql=CONCAT('SELECT 收盘价 INTO @c6 FROM ',srcode_b6,' WHERE ',srcode_b6,'.varId =139 and timeint<= '
        ,todayNowHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    prepare stmt from @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    deallocate prepare stmt;     -- 释放掉预处理段
    SET c6=@c6;


SET @v_sql=CONCAT('SELECT 收盘价 INTO @e6 FROM ',srcode_d6,' WHERE ',srcode_d6,'.varId =139 and timeint<= '
        ,todayNowHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    prepare stmt from @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    deallocate prepare stmt;     -- 释放掉预处理段
    SET e6=@e6;


SET d3=b5,d4=b6,d5=b6,d8='主力连续',d9='主力连续',d10='主力连续';
SET e3=c5,e4=c6,e5=c6; -- ,@e8=@c6,@e9=@c6,@e10=@c6 此处有修改
SET o3=n5,o4=n6,o5=n6;


-- 查主力连续code 用作时间的计算
SELECT `code` INTO code_date FROM CX_MarketData_2FH WHERE CX_MarketData_2FH.VarId = 139 AND TimeInt > todayStartHour ORDER BY EditTime DESC LIMIT 1;
SET code_date = SUBSTRING(code_date,LENGTH(code_date)-4,4);-- 截取字符串
SET my_year = code_date/100,my_month = code_date - my_year * 100;-- 计算年份 月份
SET o8 = CAST(((2000+my_year)*10000+my_month*100+15) AS DATETIME);
SET o9=o8,o10=o8;

SELECT 收盘价 INTO e8 FROM CX_MarketData_2FH WHERE CX_MarketData_2FH.VarId = 139 AND TimeInt > todayStartHour ORDER BY EditTime DESC LIMIT 1;
SET e9 = e8, e10 = e8;



-- 看看ice数据需要取哪个,ice合约月份为1，3,5,7,10,交割日期为合约月份前一个月的月底，切换时间也是。1月份数据很少，所以基本不会看
 -- 10.1至2.28  看3月合约。  3456月看下一号合约，789看10月合约


IF (month>=3 and month<5)
    THEN
    set icecode1 = 'CX_MarketData_7FH';
    set icecode2 = 'CX_MarketData_8FH';
    set icemonthtmp1 = 5;
    set icemonthtmp2 = 7;
ELSEIF(month>=5 and month<7)
    THEN
    set icecode1 = 'CX_MarketData_8FH';
    set icecode2 = 'CX_MarketData_4FH';
    set icemonthtmp1 = 7;
    set icemonthtmp2 = 10;
ELSEIF(month>=7 and month<10)
    THEN
    set icecode1 = 'CX_MarketData_4FH';
    set icecode2 = 'CX_MarketData_6FH';
    set icemonthtmp1 = 10;
    set icemonthtmp2 = 3;
ELSE
    set icecode1 = 'CX_MarketData_6FH';
    set icecode2 = 'CX_MarketData_7FH';
    set icemonthtmp1 = 3;
    set icemonthtmp2 = 5;
    -- SELECT @n7=CONVERT(datetime,convert(varchar(50),(@year+1)*10000+2*100+28),112)  ---为方便计算，直接取下一年2月28日交割
END IF;
SET b7=CONCAT('ICE',icemonthtmp1,'月');
set d7=CONCAT('ICE',icemonthtmp2,'月');
SET n7=DATE_ADD(DATE_ADD(SYSDATE(), INTERVAL ((icemonthtmp1-month+12)%12) MONTH),
    interval -DAY(DATE_ADD(SYSDATE(), INTERVAL ((icemonthtmp1-month+12)%12) MONTH)) DAY);
SET o7=DATE_ADD(DATE_ADD(SYSDATE(), INTERVAL ((icemonthtmp2-month+12)%12) MONTH),
    interval -DAY(DATE_ADD(SYSDATE(), INTERVAL ((icemonthtmp2-month+12)%12) MONTH)) DAY);
-- SELECT @n7,@o7

SET b10=b7;
SET n10=n7;

SET @v_sql=CONCAT('SELECT 收盘价 INTO @c7 FROM ',icecode1,' WHERE ',icecode1,'.varid =152 AND timeint<= ',todayNowHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    prepare stmt from @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    deallocate prepare stmt;     -- 释放掉预处理段
    SET c7=@c7;

SET @v_sql=CONCAT('SELECT 收盘价 INTO @e7 FROM ',icecode2,' WHERE ',icecode2,'.varid =152 AND timeint<= ',todayNowHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    prepare stmt from @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    deallocate prepare stmt;     -- 释放掉预处理段
    SET e7=@e7;
SET c10=c7;



-- 巴西和泰国现货价格
SELECT 升贴水 INTO shengtieshuiBaxi FROM CX_ImportSugarBrazil WHERE CX_ImportSugarBrazil.varid =4 AND timeint<= today ORDER BY timeint DESC,edittime DESC LIMIT 1;
SELECT 升贴水 INTO shengtieshuiTaiguo  FROM CX_ImportSugarThailand WHERE CX_ImportSugarThailand.varid =4 AND timeint<= today ORDER BY timeint DESC,edittime DESC LIMIT 1;

SET c8=c7+shengtieshuiBaxi,c9=c7+shengtieshuiTaiguo;

-- 计算价差
SET f3=e3-c3, f4=e4-c4, f5=e5-c5, f6=e6-c6, f7=e7-c7;
SET f8=e8-c8*PtPerT*exchangeRateDollar,
       f9=e9-c9*PtPerT*exchangeRateDollar,
      -- @f10=@e10/@PtPerT/@exchangeRateDollar-@c10;
       f10 = e10 - c10 * PtPerT * exchangeRateDollar; -- 依照exl文档修改

-- 计算变化
SET @v_sql = CONCAT('SELECT 价差 INTO @jiachaLast FROM CX_MarketData_DayAnalyse_Sugar WHERE 监测序号=3 AND timeint<',todayStartHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    prepare stmt from @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    deallocate prepare stmt;     -- 释放掉预处理段
    SET jiachaLast=@jiachaLast;
IF(jiachaLast=0) THEN
    SET g3 = NULL;
ELSE
    SET g3=f3/jiachaLast-1;
END IF;
-- SELECT @sql
-- SELECT @jiachaLast
SET @v_sql = CONCAT('SELECT 价差 INTO @jiachaLast FROM ','CX_MarketData_DayAnalyse_Sugar',' WHERE 监测序号=4 AND timeint<',todayStartHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    prepare stmt from @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    deallocate prepare stmt;     -- 释放掉预处理段
    SET jiachaLast=@jiachaLast;
IF(jiachaLast=0) THEN
    SET g4 = NULL;
ELSE
    SET g4=f4/jiachaLast-1;
END IF;
SET @v_sql = CONCAT('SELECT 价差 INTO @jiachaLast FROM ','CX_MarketData_DayAnalyse_Sugar',' where 监测序号=5 and timeint<',todayStartHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    PREPARE stmt FROM @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    DEALLOCATE PREPARE stmt;     -- 释放掉预处理段
    SET jiachaLast=@jiachaLast;
if(jiachaLast=0) THEN
    SET g5 = NULL;
ELSE
    SET g5=f5/jiachaLast-1;
END IF;
SET @v_sql = CONCAT('SELECT 价差 INTO @jiachaLast FROM ','CX_MarketData_DayAnalyse_Sugar',' WHERE 监测序号=6 AND timeint<',todayStartHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    PREPARE stmt FROM @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    DEALLOCATE PREPARE stmt;     -- 释放掉预处理段
    SET jiachaLast=@jiachaLast;
IF(jiachaLast=0) THEN
    SET g6 = NULL;
ELSE
    SET g6=f6/jiachaLast-1;
END IF;
SET @v_sql = CONCAT('SELECT 价差 INTO @jiachaLast FROM ','CX_MarketData_DayAnalyse_Sugar',' WHERE 监测序号=7 and timeint<',todayStartHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    PREPARE stmt FROM @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    DEALLOCATE PREPARE stmt;     -- 释放掉预处理段
    SET jiachaLast=@jiachaLast;
if(jiachaLast=0) THEN
    SET g7 = NULL;
ELSE
    SET g7=f7/jiachaLast-1;
END IF;
SET @v_sql = CONCAT('SELECT 价差 INTO @jiachaLast from ','CX_MarketData_DayAnalyse_Sugar',' WHERE 监测序号=8 AND timeint<',todayStartHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    PREPARE stmt FROM @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    DEALLOCATE PREPARE stmt;     -- 释放掉预处理段
    SET jiachaLast=@jiachaLast;
IF(jiachaLast=0) THEN
    SET g8 = NULL;
ELSE
    SET g8=f8/jiachaLast-1;
END IF;
SET @v_sql = CONCAT('SELECT 价差 INTO @jiachaLast FROM ','CX_MarketData_DayAnalyse_Sugar',' WHERE 监测序号=9 AND timeint<',todayStartHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    PREPARE stmt FROM @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    DEALLOCATE PREPARE stmt;     -- 释放掉预处理段
    SET jiachaLast=@jiachaLast;
IF(jiachaLast=0) THEN
    SET g9 = NULL;
ELSE
    SET g9=f9/jiachaLast-1;
END IF;
SET @v_sql = CONCAT('SELECT 价差 INTO @jiachaLast FROM ','CX_MarketData_DayAnalyse_Sugar',' WHERE 监测序号=10 AND timeint<',todayStartHour,' ORDER BY timeint DESC,edittime DESC LIMIT 1');
    PREPARE stmt FROM @v_sql;  -- 预处理需要执行的动态SQL，其中stmt是一个变量
    EXECUTE stmt;      -- 执行SQL语句
    DEALLOCATE PREPARE stmt;     -- 释放掉预处理段
    SET jiachaLast=@jiachaLast;
IF(jiachaLast=0) THEN
    SET g10= NULL;
ELSE
    SET g10 =f10/jiachaLast-1;
END IF;

-- 计算持有时间
SET h3=TIMESTAMPDIFF(DAY,n3,o3);
SET h4=TIMESTAMPDIFF(DAY,n4,o4);
SET h5=TIMESTAMPDIFF(DAY,n5,o5);
SET h6=TIMESTAMPDIFF(DAY,n6,o6);
SET h7=TIMESTAMPDIFF(DAY,n7,o7);
SET h8=TIMESTAMPDIFF(DAY,n8,o8);
SET h9=TIMESTAMPDIFF(DAY,n9,o9);
SET h10=TIMESTAMPDIFF(DAY,n10,o10);

-- 计算资金成本
SET i3 = (c3 + e3 * futures_margin_per) * fundCostInternal * h3 / daysPerYear;
SET i4 = (c4 + e4 * futures_margin_per) * fundCostInternal * h4 / daysPerYear;
SET i5 = (c5 + e5) * futures_margin_per * fundCostInternal * h5 / daysPerYear;
SET i6 = (c6 + e6) * futures_margin_per * fundCostInternal * h6 / daysPerYear;
SET i7 = (c7 + e7) * funCostForeign * futures_margin_per_for_Internal * h7 / daysPerYear;
SET i8 = h8 / daysPerYear *(funCostForeign*c8*exchangeRateDollar*PtPerT + e8 * futures_margin_per * fundCostInternal);
SET i9 = h9 / daysPerYear *(funCostForeign*c9*exchangeRateDollar*PtPerT + e9 * futures_margin_per * fundCostInternal);
SET i10 = h10 / daysPerYear * (funCostForeign * futures_margin_per_for_Internal * c10 * exchangeRateDollar * PtPerT + 10 * futures_margin_per * fundCostInternal); --


-- 计算仓储费用
SET j3=h3*storeCostInternal;
SET j4=h4*storeCostInternal;
SET j5=h5*storeCostInternal;
SET j6=h6*storeCostInternal;
SET j7=h7*storeCostForeign/PtPerT;
SET j8=(H8-shipTime)*storeCostInternal;
SET j9=(H9-shipTime)*storeCostInternal;
SET j10=(H10-shipTime)*storeCostInternal;

-- 计算加工/损耗

-- -FOB(离岸价) ＝（原糖价+升水）×重量单位换算值×（1+旋光度增值）。
-- -原糖到岸价（税后）=((FOB+运费）*（1+保险费率）*汇率*（1+贸易代理费率）*（1+银行手续费率）+利息及劳务费）*（1+关税）*（1+增值税）
-- -加工/损耗=原糖到岸价*（1+原糖加工损耗）+加工费

SET FOB8 = c8*PtPerT*(1+xuanguangduRaise);
SET daoanjia8 = ((FOB8+q8)*(1+InsuranceCost)*exchangeRateDollar*(1+TradeAgentCost)*(1+FeesOfBanks)+interestAndLaborFee)*(1+tariffAdditional)*(1+addedValueTax);
SET k8=daoanjia8*(sugarProcLossBrazil)+processingCost;

SET FOB9 = c9*PtPerT*(1+xuanguangduRaise);
SET daoanjia9 = ((FOB9+q9)*(1+InsuranceCost)*exchangeRateDollar*(1+TradeAgentCost)*(1+FeesOfBanks)+interestAndLaborFee)*(1+tariffAdditional)*(1+addedValueTax);
SET k9=daoanjia9*(sugarProcLossBrazil)+processingCost;

SET FOB10 = c10*PtPerT*(1+xuanguangduRaise);
SET daoanjia10 = ((FOB10+q10)*(1+InsuranceCost)*exchangeRateDollar*(1+TradeAgentCost)*(1+FeesOfBanks)+interestAndLaborFee)*(1+tariffAdditional)*(1+addedValueTax);
SET k10=daoanjia10*(sugarProcLossBrazil)+processingCost;


-- 计算其他费用
-- 其他=原糖到岸价（税后）-FOB（离岸价）
-- 说明:1.@PtPerT=22.0462 重量转换比例  2.@xuanguangduRaise=0.0275,---旋光度增值
    -- 3.@InsuranceCost=0.004616,---保险费率   4.@exchangeRateDollar=6.15,---人民币汇率
    -- 5.@TradeAgentCost=0.003,---贸易代理费率   6.@FeesOfBanks=0.00125,----银行手续费率
    -- 7.@interestAndLaborFee=25,--利息及劳务费 8.@tariffAdditional=0.5,---配额外关税 9.@addedValueTax=0.17,  ---增值税
-- 根据excel编写公式
SET l8=daoanjia8-FOB8;
SET l9=daoanjia9-FOB9;
SET l10=daoanjia10-FOB10;

    
-- 计算总计成本
SET m3 = i3+j3+k3+l3;
SET m4 = i4+j4+k4+l4;
SET m5 = i5+j5+k5+l5;
SET m6 = i6+j6+k6+l6;
SET m7 = i7+j7+k7+l7;
SET m8 = i8+j8+k8+l8;
SET m9 = i9+j9+k9+l9;
SET m10 = i10+j10+k10+l10;


INSERT INTO CX_MarketData_DayAnalyse_Sugar SELECT NULL,SYSDATE(),4,todayNowHour,3,a3,b3,c3,d3,e3,f3,g3,h3,i3,j3,k3,l3,m3,n3,o3;
INSERT INTO CX_MarketData_DayAnalyse_Sugar SELECT NULL,SYSDATE(),4,todayNowHour,4,a4,b4,c4,d4,e4,f4,g4,h4,i4,j4,k4,l4,m4,n4,o4;
INSERT INTO CX_MarketData_DayAnalyse_Sugar SELECT NULL,SYSDATE(),4,todayNowHour,5,a5,b5,c5,d5,e5,f5,g5,h5,i5,j5,k5,l5,m5,n5,o5;
INSERT INTO CX_MarketData_DayAnalyse_Sugar SELECT NULL,SYSDATE(),4,todayNowHour,6,a6,b6,c6,d6,e6,f6,g6,h6,i6,j6,k6,l6,m6,n6,o6;
INSERT INTO CX_MarketData_DayAnalyse_Sugar SELECT NULL,SYSDATE(),4,todayNowHour,7,a7,b7,c7,d7,e7,f7,g7,h7,i7,j7,k7,l7,m7,n7,o7;
INSERT INTO CX_MarketData_DayAnalyse_Sugar SELECT NULL,SYSDATE(),4,todayNowHour,8,a8,b8,c8,d8,e8,f8,g8,h8,i8,j8,k8,l8,m8,n8,o8;
INSERT INTO CX_MarketData_DayAnalyse_Sugar SELECT NULL,SYSDATE(),4,todayNowHour,9,a9,b9,c9,d9,e9,f9,g9,h9,i9,j9,k9,l9,m9,n9,o9;
INSERT INTO CX_MarketData_DayAnalyse_Sugar SELECT NULL,SYSDATE(),4,todayNowHour,10,a10,b10,c10,d10,e10,f10,g10,h10,i10,j10,k10,l10,m10,n10,o10;

END $$
DELIMITER ;