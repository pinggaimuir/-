/****** 计算四季豆在全国各个省份的批发均价，并且保存到对应表格中  ******/
 alter proc cal_fruits_wholes_greenbean 
 @today varchar(50),@var varchar(50),@table varchar(50),@tableDB varchar(50),@varDB varchar(50)
 as 
 begin
 declare @sql nvarchar(1000)
 
 --生成临时表
 IF OBJECT_ID('[bricdata].[dbo].[fruits_whole_temp]') IS null
 create table [bricdata].[dbo].[fruits_whole_temp] (province varchar(20),price float)
 TRUNCATE  table [bricdata].[dbo].[fruits_whole_temp] 

--统计有无当天的数据
  declare @num int
  set @sql = N'select @num=count(*) from  '+@table +
  ' where VarId='+@var+' and TimeInt='+@today+' and province is not null'
  exec sp_executesql @sql,N'@num int output',@num output
  if ( @num<1 )return
 --计算各个地方的平均价  
 set @sql = 'insert into fruits_whole_temp '+
 ' SELECT province,AVG(平均价) FROM '+ @table+ ' where VarId='+@var+' and TimeInt='+ @today +
    'and province is not null group by Province'
 exec (@sql)
 --计算全国平均价  
 declare @average float
 select @average= avg(price)  from fruits_whole_temp
 insert into fruits_whole_temp(province,price) values( '全国',@average)
---将列转换成行存储
set @sql= 'insert into '+@tableDB+  
' select GETDATE() edittime,'+@varDB+' varid,'+@today+' timeint,* from [bricdata].[dbo].[fruits_whole_temp] as p pivot(max(price) for province 
in(全国 ,北京,天津,河北,山西,内蒙古,辽宁,吉林,黑龙江,上海,江苏,浙江,安徽,福建,江西,山东,河南,湖北,湖南
   ,广东,广西,海南,重庆,四川,贵州 ,云南,西藏,陕西,甘肃,青海 ,宁夏,新疆))a'
exec (@sql)

end