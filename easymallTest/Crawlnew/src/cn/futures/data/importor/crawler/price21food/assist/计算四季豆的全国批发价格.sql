/****** 计算四季豆在全国各个省份的批发均价，并且保存到对应表格中  ******/
DELIMITER $$
CREATE PROCEDURE cal_fruits_wholes_greenbean(
 IN today VARCHAR(50),IN var VARCHAR(50),IN table1 VARCHAR(50),
 IN tableDB VARCHAR(50),IN varDB varchar(50))
  label_pro:BEGIN
  DECLARE v_sql NVARCHAR(1000);
  DECLARE num INT;
  DECLARE average FLOAT;

  -- 统计有无当天的数据
  SET @v_sql = CONCAT('SELECT count(*) INTO @num FROM  ',table1,
                      ' WHERE ',table1,'.VarId=',var,' and TimeInt=',today,' and province is not null AND province <> ''''');
  PREPARE stmt FROM @v_sql;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;
  SET num=@num;
  IF num<1 THEN
   LEAVE label_pro;
  END IF;
  -- 计算各个地方的平均价-- 生成临时表
  SET @v_sql =CONCAT('CREATE TEMPORARY TABLE fruits_whole_temp(',
                     ' SELECT province,AVG(平均价) price FROM ',table1,' where ',table1,'.VarId=',var,' and TimeInt=',today,
                     'AND province IS NOT NULL AND province <> '''' GROUP BY Province)');
  PREPARE stmt FROM @v_sql;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;
  -- 计算全国平均价
  SELECT avg(price) INTO average from fruits_whole_temp;
  INSERT INTO fruits_whole_temp(province,price) values( '全国',average);
  -- 将列转换成行存储
SET @v_sql= CONCAT('INSERT INTO ',tableDB,
                     'SELECT
                     SYSDATE() AS edittime,
                     ',varDB,' AS varid,
  ',today,' AS timeint,
  SUM(IF(province=全国,price,NULL )) AS 全国,
  SUM(IF(province=北京,price,NULL )) AS 北京,
  SUM(IF(province=天津,price,NULL )) AS 天津,
  SUM(IF(province=河北,price,NULL )) AS 河北,
  SUM(IF(province=山西,price,NULL )) AS 山西,
  SUM(IF(province=内蒙古,price,NULL )) AS 内蒙古,
  SUM(IF(province=辽宁,price,NULL )) AS 辽宁,
  SUM(IF(province=吉林,price,NULL )) AS 吉林,
  SUM(IF(province=黑龙江,price,NULL )) AS 黑龙江,
  SUM(IF(province=上海,price,NULL )) AS 上海,
  SUM(IF(province=江苏,price,NULL )) AS 江苏,
  SUM(IF(province=浙江,price,NULL )) AS 浙江,
  SUM(IF(province=安徽,price,NULL )) AS 安徽,
  SUM(IF(province=福建,price,NULL )) AS 福建,
  SUM(IF(province=江西,price,NULL )) AS 江西,
  SUM(IF(province=山东,price,NULL )) AS 山东,
  SUM(IF(province=河南,price,NULL )) AS 河南,
  SUM(IF(province=湖北,price,NULL )) AS 湖北,
  SUM(IF(province=湖南,price,NULL )) AS 湖南,
  SUM(IF(province=广东,price,NULL )) AS 广东,
  SUM(IF(province=广西,price,NULL )) AS 广西,
  SUM(IF(province=海南,price,NULL )) AS 海南,
  SUM(IF(province=重庆,price,NULL )) AS 重庆,
  SUM(IF(province=四川,price,NULL )) AS 四川,
  SUM(IF(province=贵州,price,NULL )) AS 贵州,
  SUM(IF(province=云南,price,NULL )) AS 云南,
  SUM(IF(province=西藏,price,NULL )) AS 西藏,
  SUM(IF(province=陕西,price,NULL )) AS 陕西,
  SUM(IF(province=甘肃,price,NULL )) AS 甘肃,
  SUM(IF(province=青海,price,NULL )) AS 青海,
  SUM(IF(province=宁夏,price,NULL )) AS 宁夏,
  SUM(IF(province=新疆,price,NULL )) AS 新疆 FROM
  (SELECT province,MAX(price) FROM bricdata.fruits_whole_temp GROUP BY province) AS temp');
  PREPARE stmt FROM @v_sql;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  DROP TEMPORARY TABLE fruits_whole_temp;
 END $$
DELIMITER ;
