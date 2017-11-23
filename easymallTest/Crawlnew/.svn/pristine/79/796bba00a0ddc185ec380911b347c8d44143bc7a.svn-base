alter proc dayAnalyse_sugar as begin

declare @today int, @todaystr varchar(20),@todayNowHour varchar(20),@todayStartHour varchar(20)
declare @year int,@month int,@day int,@hour int;
select @year = YEAR(getdate()),@month=MONTH(getdate()),@day=DAY(getdate()),@hour=DATEPART(hh,getdate())
select @today=@year*10000+@month*100+@day
select @todaystr = convert(varchar(20),@today)
select @todayNowHour = convert(varchar(20),@today*100+@hour)
select @todayStartHour = @todaystr+'00'

declare @funCostForeign float,@storeCostForeign float,@fundCostInternal float,@storeCostInternal float
declare @shipTime float,@daysPerYear int,@processingCost float,@exchangeRateDollar float,@sugarProcLossTailand float,
@sugarProcLossBrazil float,
@tariffAdditional float,
@tariffNormal float,
@addedValueTax float,
@interestAndLaborFee float,
@FeesOfBanks float,
@TradeAgentCost float,
@InsuranceCost float,
@xuanguangduRaise float,
@PtPerT float,
@KgPerPt float,
@q8 float, ---运费
@q9 float,
@q10 float


select @funCostForeign=0.04,
@storeCostForeign=0.03,
@fundCostInternal=0.08,
@storeCostInternal=0.45,
@shipTime=45,
@daysPerYear=360,
@processingCost=400,  ---加工费
@exchangeRateDollar=6.15,
@sugarProcLossTailand=0.04,
@sugarProcLossBrazil=0.035,
@tariffAdditional=0.5,---配额外关税
@tariffNormal=0.15,   ---配额内关税
@addedValueTax=0.17,  ---增值税
@interestAndLaborFee=25,--利息及劳务费
@FeesOfBanks=0.00125,----银行手续费率
@TradeAgentCost=0.003,---贸易代理费率
@InsuranceCost=0.004616,---保险费率
@xuanguangduRaise=0.0275,---旋光度增值
@PtPerT=22.0462,   ---重量转换比例
@KgPerPt=0.454,     ---重量转换比例
@q8=42, 
@q9 = 25,
@q10 =33.5

declare @a9 varchar(20), @a10 varchar(20), @a3 varchar(20), @a4 varchar(20), @a5 varchar(20), @a6 varchar(20), @a7 varchar(20), @a8 varchar(20);
declare @b10 varchar(20), @b9 varchar(20), @b3 varchar(20), @b4 varchar(20), @b5 varchar(20), @b6 varchar(20), @b7 varchar(20), @b8 varchar(20);
declare @c10 float, @c9 float, @c3 float, @c4 float, @c5 float, @c6 float, @c7 float, @c8 float;
declare @d10 varchar(20), @d9 varchar(20), @d3 varchar(20), @d4 varchar(20), @d5 varchar(20), @d6 varchar(20), @d7 varchar(20), @d8 varchar(20);
declare @e10 float, @e9 float, @e3 float, @e4 float, @e5 float, @e6 float, @e7 float, @e8 float;
declare @f10 float, @f9 float, @f3 float, @f4 float, @f5 float, @f6 float, @f7 float, @f8 float;
declare @g10 float, @g9 float, @g3 float, @g4 float, @g5 float, @g6 float, @g7 float, @g8 float;
declare @h10 float, @h9 float, @h3 float, @h4 float, @h5 float, @h6 float, @h7 float, @h8 float;
declare @i10 float, @i9 float, @i3 float, @i4 float, @i5 float, @i6 float, @i7 float, @i8 float;
declare @j10 float, @j9 float, @j3 float, @j4 float, @j5 float, @j6 float, @j7 float, @j8 float;
declare @k10 float, @k9 float, @k3 float, @k4 float, @k5 float, @k6 float, @k7 float, @k8 float;
declare @l10 float, @l9 float, @l3 float, @l4 float, @l5 float, @l6 float, @l7 float, @l8 float;
declare @m10 float, @m9 float, @m3 float, @m4 float, @m5 float, @m6 float, @m7 float, @m8 float;
declare @n10 datetime, @n9 datetime, @n3 datetime, @n4 datetime, @n5 datetime, @n6 datetime, @n7 datetime, @n8 datetime;
declare @o10 datetime, @o9 datetime, @o3 datetime, @o4 datetime, @o5 datetime, @o6 datetime, @o7 datetime, @o8 datetime;

select @a3='期现套利',@a4='期现套利',@a5='跨期套利',@a6='跨期套利',@a7='跨期套利',@a8='跨市套利',@a9='跨市套利',@a10='跨市套利';
select @b3='广西现货',@b4='广西现货',@b8='巴西现货',@b9='泰国现货'
select @n3=GETDATE(),@n4=GETDATE(),@n8=GETDATE(),@n9=GETDATE();
select @k3=10, @k4=30, @k5=20, @k6=20, @k7=0
select @l3=20, @l4=20, @l5=20, @l6=20, @l7=0.2



--以上是需要用到的变量的定义，以下开始计算表
---广西现货数据
select top 1 @c3=  广西 from CX_Price where varId=4 and timeint<=@today order by timeint desc,edittime desc
select @c4=@c3
---计算几个需要取期货数据的地方从哪个合约取数据
declare @srcode_b5 varchar(200),@srcode_b6 varchar(200),@srcode_d6 varchar(200)
if (@today%10000>1215 or @today%10000<=415 ) begin
    set @srcode_b5 = 'CX_MarketData_5FH'
    set @srcode_b6 = 'CX_MarketData_7FH'
    set @srcode_d6 = 'CX_MarketData_3FH'
    if(@today%10000>1215) begin
        set @b5='SR'+convert(varchar(5),@year%10+1)+'05'
        set @n5=convert(datetime,convert(varchar(50),(@year+1)*10000+5*100+15),112)
        set @b6='SR'+CONVERT(varchar(5),@year%10+1)+'09'
        set @n6=convert(datetime,convert(varchar(50),(@year+1)*10000+9*100+15),112)
        set @d6='SR'+CONVERT(varchar(5),@year%10+2)+'01'
        set @o6=CONVERT(datetime,convert(varchar(50),(@year+2)*10000+1*100+15),112)
        ---set @n5=
    end else begin
        set @b5='SR'+convert(varchar(5),@year%10)+'05'
        set @n5=convert(datetime,convert(varchar(50),(@year)*10000+5*100+15),112)
        set @b6='SR'+CONVERT(varchar(5),@year%10)+'09'
        set @n6=convert(datetime,convert(varchar(50),(@year)*10000+9*100+15),112)
        set @d6='SR'+CONVERT(varchar(5),@year%10+1)+'01'
        set @o6=convert(datetime,convert(varchar(50),(@year+1)*10000+1*100+15),112)
    end
end
else if(@today%10000>415 and @today%10000<=815 ) begin
    set @srcode_b5 = 'CX_MarketData_7FH'
    set @srcode_b6 = 'CX_MarketData_3FH'
    set @srcode_d6 = 'CX_MarketData_5FH'
    set @b5='SR'+convert(varchar(5),@year%10)+'09'
    set @n5=convert(datetime,convert(varchar(50),(@year)*10000+9*100+15),112)
    set @b6='SR'+CONVERT(varchar(5),@year%10+1)+'01'
    set @n6=convert(datetime,convert(varchar(50),(@year+1)*10000+1*100+15),112)
    set @d6='SR'+CONVERT(varchar(5),@year%10+1)+'05'
    set @o6=convert(datetime,convert(varchar(50),(@year+1)*10000+5*100+15),112)
end
else begin
    set @srcode_b5 = 'CX_MarketData_3FH'
    set @srcode_b6 = 'CX_MarketData_5FH'
    set @srcode_d6  = 'CX_MarketData_7FH'
    set @b5='SR'+convert(varchar(5),@year%10+1)+'01'
    set @n5=convert(datetime,convert(varchar(50),(@year+1)*10000+1*100+15),112)
    set @b6='SR'+CONVERT(varchar(5),@year%10+1)+'05'
    set @n6=convert(datetime,convert(varchar(50),(@year+1)*10000+5*100+15),112)
    set @d6='SR'+CONVERT(varchar(5),@year%10+1)+'09'
    set @o6=convert(datetime,convert(varchar(50),(@year+1)*10000+9*100+15),112)
end
declare @sql nvarchar(1000)
set @sql='select top 1 @c5=结算价 from '+@srcode_b5+' where varid =139 and timeint<= '+@todayNowHour+' order by timeint desc,edittime desc'
--select @sql
exec sp_executesql @sql,N'@c5 float output',@c5 output

set @sql='select top 1 @c6=结算价 from '+@srcode_b6+' where varid =139 and timeint<= '+@todayNowHour+' order by timeint desc,edittime desc'
--select @sql
exec sp_executesql @sql,N'@c6 float output',@c6 output

set @sql='select top 1 @e6=结算价 from '+@srcode_d6+' where varid =139 and timeint<= '+@todayNowHour+' order by timeint desc,edittime desc'
exec sp_executesql @sql,N'@e6 float output',@e6 output


select @d3=@b5,@d4=@b6,@d5=@b6,@d8=@b6,@d9=@b6,@d10=@b6;
select @e3=@c5,@e4=@c6,@e5=@c6,@e8=@c6,@e9=@c6,@e10=@c6;
select @o3=@n5,@o4=@n6,@o5=@n6,@o8=@n6,@o9=@n6,@o10=@n6;


---看看ice数据需要取哪个,ice合约月份为1，3,5,7,10,交割日期为合约月份前一个月的月底，切换时间也是。1月份数据很少，所以基本不会看
 ---10.1至2.28  看3月合约。  3456月看下一号合约，789看10月合约
declare @icecode1 varchar(20),@icecode2 varchar(20);
declare @icemonthtmp1 int,@icemonthtmp2 int;

if(@month>=3 and @month<5) begin
    set @icecode1 = 'CX_MarketData_7FH'
    set @icecode2 = 'CX_MarketData_8FH'
    set @icemonthtmp1 = 5
    set @icemonthtmp2 = 7
end else if(@month>=5 and @month<7) begin
    set @icecode1 = 'CX_MarketData_8FH'
    set @icecode2 = 'CX_MarketData_4FH'
    set @icemonthtmp1 = 7
    set @icemonthtmp2 = 10
end else if(@month>=7 and @month<10) begin
    set @icecode1 = 'CX_MarketData_4FH'
    set @icecode2 = 'CX_MarketData_6FH'
    set @icemonthtmp1 = 10
    set @icemonthtmp2 = 3
end else begin
    set @icecode1 = 'CX_MarketData_6FH'
    set @icecode2 = 'CX_MarketData_7FH'
    set @icemonthtmp1 = 3
    set @icemonthtmp2 = 5
    ---select @n7=CONVERT(datetime,convert(varchar(50),(@year+1)*10000+2*100+28),112)  ---为方便计算，直接取下一年2月28日交割
end
set @b7='ICE'+CONVERT(varchar(5),@icemonthtmp1)+'月'
set @d7='ICE'+CONVERT(varchar(5),@icemonthtmp2)+'月'
select @n7=dateadd(dd,-day(dateadd(month,(@icemonthtmp1-@month+12)%12,getdate())),dateadd(month,(@icemonthtmp1-@month+12)%12,getdate())) 
select @o7=dateadd(dd,-day(dateadd(month,(@icemonthtmp2-@month+12)%12,getdate())),dateadd(month,(@icemonthtmp2-@month+12)%12,getdate())) 
--select @n7,@o7

select @b10=@b7;
select @n10=@n7;

set @sql='select top 1 @c7=收盘价 from '+@icecode1+' where varid =152 and timeint<= '+@todayNowHour+' order by timeint desc,edittime desc'
exec sp_executesql @sql,N'@c7 float output',@c7 output

set @sql='select top 1 @e7=收盘价 from '+@icecode2+' where varid =152 and timeint<= '+@todayNowHour+' order by timeint desc,edittime desc'
exec sp_executesql @sql,N'@e7 float output',@e7 output
select @c10=@c7;



---巴西和泰国现货价格
declare @shengtieshuiBaxi float,@shengtieshuiTaiguo float;
select top 1 @shengtieshuiBaxi = 升贴水 from CX_ImportSugarBrazil where varid =4 and timeint<= @today order by timeint desc,edittime desc
select top 1 @shengtieshuiTaiguo = 升贴水 from CX_ImportSugarThailand where varid =4 and timeint<= @today order by timeint desc,edittime desc
select @c8=@c7+@shengtieshuiBaxi,@c9=@c7+@shengtieshuiTaiguo

--计算价差
select @f3=@e3-@c3, @f4=@e4-@c4, @f5=@e5-@c5, @f6=@e6-@c6, @f7=@e7-@c7;
select @f8=@e8-@c8*@PtPerT*@exchangeRateDollar,@f9=@e9-@c9*@PtPerT*@exchangeRateDollar,@f10=@e10/@PtPerT/@exchangeRateDollar-@c10;

--计算变化
declare @jiachaLast float;
select @sql = 'select top 1 @jiachaLast = 价差 from '+'CX_MarketData_DayAnalyse_Sugar'+' where 监测序号=3 and timeint<'+@todayStartHour+' order by timeint desc,edittime desc'
exec sp_executesql @sql,N'@jiachaLast float output',@jiachaLast output
if(@jiachaLast=0) begin
    select @g3 = null
end else begin 
    select @g3=@f3/@jiachaLast-1;
end
--select @sql
--select @jiachaLast
select @sql = 'select top 1 @jiachaLast = 价差 from '+'CX_MarketData_DayAnalyse_Sugar'+' where 监测序号=4 and timeint<'+@todayStartHour+' order by timeint desc,edittime desc'
exec sp_executesql @sql,N'@jiachaLast float output',@jiachaLast output
if(@jiachaLast=0) begin
    select @g4 = null
end else begin 
    select @g4=@f4/@jiachaLast-1;
end
select @sql = 'select top 1 @jiachaLast = 价差 from '+'CX_MarketData_DayAnalyse_Sugar'+' where 监测序号=5 and timeint<'+@todayStartHour+' order by timeint desc,edittime desc'
exec sp_executesql @sql,N'@jiachaLast float output',@jiachaLast output
if(@jiachaLast=0) begin
    select @g5 = null
end else begin 
    select @g5=@f5/@jiachaLast-1;
end
select @sql = 'select top 1 @jiachaLast = 价差 from '+'CX_MarketData_DayAnalyse_Sugar'+' where 监测序号=6 and timeint<'+@todayStartHour+' order by timeint desc,edittime desc'
exec sp_executesql @sql,N'@jiachaLast float output',@jiachaLast output
if(@jiachaLast=0) begin
    select @g6 = null
end else begin 
    select @g6=@f6/@jiachaLast-1;
end
select @sql = 'select top 1 @jiachaLast = 价差 from '+'CX_MarketData_DayAnalyse_Sugar'+' where 监测序号=7 and timeint<'+@todayStartHour+' order by timeint desc,edittime desc'
exec sp_executesql @sql,N'@jiachaLast float output',@jiachaLast output
if(@jiachaLast=0) begin
    select @g7 = null
end else begin 
    select @g7=@f7/@jiachaLast-1;
end
select @sql = 'select top 1 @jiachaLast = 价差 from '+'CX_MarketData_DayAnalyse_Sugar'+' where 监测序号=8 and timeint<'+@todayStartHour+' order by timeint desc,edittime desc'
exec sp_executesql @sql,N'@jiachaLast float output',@jiachaLast output
if(@jiachaLast=0) begin
    select @g8 = null
end else begin 
    select @g8=@f8/@jiachaLast-1;
end
select @sql = 'select top 1 @jiachaLast = 价差 from '+'CX_MarketData_DayAnalyse_Sugar'+' where 监测序号=9 and timeint<'+@todayStartHour+' order by timeint desc,edittime desc'
exec sp_executesql @sql,N'@jiachaLast float output',@jiachaLast output
if(@jiachaLast=0) begin
    select @g9 = null
end else begin 
    select @g9=@f9/@jiachaLast-1;
end
select @sql = 'select top 1 @jiachaLast = 价差 from '+'CX_MarketData_DayAnalyse_Sugar'+' where 监测序号=10 and timeint<'+@todayStartHour+' order by timeint desc,edittime desc'
exec sp_executesql @sql,N'@jiachaLast float output',@jiachaLast output
if(@jiachaLast=0) begin
    select @g10= null
end else begin 
    select @g10 =@f10/@jiachaLast-1;
end

---计算持有时间
select @h3=datediff(dd,@n3,@o3);
select @h4=datediff(dd,@n4,@o4);
select @h5=datediff(dd,@n5,@o5);
select @h6=datediff(dd,@n7,@o6);
select @h7=datediff(dd,@n7,@o7);
select @h8=datediff(dd,@n8,@o8);
select @h9=datediff(dd,@n9,@o9);
select @h10=datediff(dd,@n10,@o10);

----计算资金成本
select @i3 = @c3*@fundCostInternal*@h3/@daysPerYear
select @i4 = @c4*@fundCostInternal*@h5/@daysPerYear
select @i5 = @c5*@fundCostInternal*@h6/@daysPerYear
select @i6 = @c6*@fundCostInternal*@h6/@daysPerYear
select @i7 = @c7*@funCostForeign*@h7/@daysPerYear
select @i8 = @h8*@funCostForeign*@c8*@exchangeRateDollar*@PtPerT/@daysPerYear
select @i9 = @h9*@funCostForeign*@c9*@exchangeRateDollar*@PtPerT/@daysPerYear
select @i10 = @h10*@funCostForeign*@c10*@exchangeRateDollar*@PtPerT/@daysPerYear

----计算仓储费用
select @j3=@h3*@storeCostInternal
select @j4=@h4*@storeCostInternal
select @j5=@h5*@storeCostInternal
select @j6=@h6*@storeCostInternal
select @j7=@h7*@storeCostForeign/@PtPerT
select @j8=(@H8-@shipTime)*@storeCostInternal
select @j9=(@H9-@shipTime)*@storeCostInternal
select @j10=(@H10-@shipTime)*@storeCostInternal

----计算加工/损耗

---FOB(离岸价) ＝（原糖价+升水）×重量单位换算值×（1+旋光度增值）。        
---原糖到岸价（税后）=((FOB+运费）*（1+保险费率）*汇率*（1+贸易代理费率）*（1+银行手续费率）+利息及劳务费）*（1+关税）*（1+增值税）     
---加工/损耗=原糖到岸价*（1+原糖加工损耗）+加工费

declare @FOB float,@daoanjia float

select @FOB = @c8*@PtPerT*(1+@xuanguangduRaise)
select @daoanjia = ((@FOB+@q8)*(1+@InsuranceCost)*@exchangeRateDollar*(1+@TradeAgentCost)*(1+@FeesOfBanks)+@interestAndLaborFee)*(1+@tariffAdditional)*(1+@addedValueTax)
select @k8=@daoanjia*(@sugarProcLossBrazil)+@processingCost
--select @k8

select @k8=(((@c8*@PtPerT*(1+@xuanguangduRaise))+@q8)*(1+@InsuranceCost)*@exchangeRateDollar*(1+@TradeAgentCost)*(1+@FeesOfBanks)
    +@interestAndLaborFee)*(1+@tariffAdditional)*(1+@addedValueTax)
   *(@sugarProcLossBrazil)+@processingCost
select @k9=(((@c9*@PtPerT*(1+@xuanguangduRaise))+@q9)*(1+@InsuranceCost)*@exchangeRateDollar*(1+@TradeAgentCost)*(1+@FeesOfBanks)
    +@interestAndLaborFee)*(1+@tariffAdditional)*(1+@addedValueTax)
   *(@sugarProcLossTailand)+@processingCost
select @k10=(((@c10*@PtPerT*(1+@xuanguangduRaise))+@q10)*(1+@InsuranceCost)*@exchangeRateDollar*(1+@TradeAgentCost)*(1+@FeesOfBanks)
    +@interestAndLaborFee)*(1+@tariffAdditional)*(1+@addedValueTax)
   *(@sugarProcLossBrazil)+@processingCost

---计算其他费用
---其他=原糖到岸价（税后）-FOB（离岸价）
select @l8=(((@c8*@PtPerT*(1+@xuanguangduRaise))+@q8)*(1+@InsuranceCost)*@exchangeRateDollar*(1+@TradeAgentCost)*(1+@FeesOfBanks)
    +@interestAndLaborFee)*(1+@tariffAdditional)*(1+@addedValueTax)
    -(@c8*@PtPerT*(1+@xuanguangduRaise))
select @l9=(((@c9*@PtPerT*(1+@xuanguangduRaise))+@q9)*(1+@InsuranceCost)*@exchangeRateDollar*(1+@TradeAgentCost)*(1+@FeesOfBanks)
    +@interestAndLaborFee)*(1+@tariffAdditional)*(1+@addedValueTax)
    -(@c9*@PtPerT*(1+@xuanguangduRaise))
select @l10=(((@c10*@PtPerT*(1+@xuanguangduRaise))+@q10)*(1+@InsuranceCost)*@exchangeRateDollar*(1+@TradeAgentCost)*(1+@FeesOfBanks)
    +@interestAndLaborFee)*(1+@tariffAdditional)*(1+@addedValueTax)
    -(@c10*@PtPerT*(1+@xuanguangduRaise))
    
---计算总计成本
select @m3 = @i3+@j3+@k3+@l3
select @m4 = @i4+@j4+@k4+@l4
select @m5 = @i5+@j5+@k5+@l5
select @m6 = @i6+@j6+@k6+@l6
select @m7 = @i7+@j7+@k7+@l7
select @m8 = @i8+@j8+@k8+@l8
select @m9 = @i9+@j9+@k9+@l9
select @m10 = @i10+@j10+@k10+@l10


insert into CX_MarketData_DayAnalyse_Sugar select getdate(),4,@todayNowHour,3,@a3,@b3,@c3,@d3,@e3,@f3,@g3,@h3,@i3,@j3,@k3,@l3,@m3,@n3,@o3
insert into CX_MarketData_DayAnalyse_Sugar select getdate(),4,@todayNowHour,4,@a4,@b4,@c4,@d4,@e4,@f4,@g4,@h4,@i4,@j4,@k4,@l4,@m4,@n4,@o4
insert into CX_MarketData_DayAnalyse_Sugar select getdate(),4,@todayNowHour,5,@a5,@b5,@c5,@d5,@e5,@f5,@g5,@h5,@i5,@j5,@k5,@l5,@m5,@n5,@o5
insert into CX_MarketData_DayAnalyse_Sugar select getdate(),4,@todayNowHour,6,@a6,@b6,@c6,@d6,@e6,@f6,@g6,@h6,@i6,@j6,@k6,@l6,@m6,@n6,@o6
insert into CX_MarketData_DayAnalyse_Sugar select getdate(),4,@todayNowHour,7,@a7,@b7,@c7,@d7,@e7,@f7,@g7,@h7,@i7,@j7,@k7,@l7,@m7,@n7,@o7
insert into CX_MarketData_DayAnalyse_Sugar select getdate(),4,@todayNowHour,8,@a8,@b8,@c8,@d8,@e8,@f8,@g8,@h8,@i8,@j8,@k8,@l8,@m8,@n8,@o8
insert into CX_MarketData_DayAnalyse_Sugar select getdate(),4,@todayNowHour,9,@a9,@b9,@c9,@d9,@e9,@f9,@g9,@h9,@i9,@j9,@k9,@l9,@m9,@n9,@o9
insert into CX_MarketData_DayAnalyse_Sugar select getdate(),4,@todayNowHour,10,@a10,@b10,@c10,@d10,@e10,@f10,@g10,@h10,@i10,@j10,@k10,@l10,@m10,@n10,@o10

end 