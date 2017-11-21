/*秒杀执行的存储过程*/
DELIMITER $$
/*定义存储过程*/
/*row_count() 返回上一条sql的影响行数
  0：未修改数据 >0：修改的行数  <0：sql错误/未执行sql*/
/*r_result返回结果 1：秒杀成功 0：秒杀已关闭  -1：重复秒杀 -2：秒杀失败 */
CREATE PROCEDURE `execute_seckill`
  (in s_seckill_id BIGINT,in s_phone BIGINT,
  in s_kill_time TIMESTAMP,out s_result INT)
  BEGIN
    DECLARE insert_count INT DEFAULT 0;
    START TRANSACTION;
    INSERT IGNORE INTO success_killed
      (seckill_id,user_phone,create_time)
      values (s_seckill_id,s_phone,s_kill_time);

    select row_count() INTO insert_count;
    IF (insert_count=0)THEN
      ROLLBACK ;
      set s_result=-1;
    ELSEIF (insert_count<0) THEN
      ROLLBACK ;
      SET s_result=-2;
    ELSE
      UPDATE seckill SET number=number-1
      WHERE seckill_id=s_seckill_id
        AND start_time<s_kill_time
        AND end_time>s_kill_time
        AND number>0;
      SELECT row_count() INTO insert_count;
      IF (insert_count = 0) THEN
        ROLLBACK ;
        set s_result = 0;
      ELSEIF (insert_count < 0) THEN
        ROLLBACK ;
        SET s_result = -2;
      ELSE
        COMMIT ;
        set s_result = 1;
      END IF;
    END IF;
  END $$
DELIMITER ;