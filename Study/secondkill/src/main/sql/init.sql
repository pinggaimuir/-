/*--创建数据库*/
CREATE DATABASE secondkill;
/*--使用数据库*/
use secondkill;
/*--创建秒杀库存表*/
CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` VARCHAR(120) NOT NULL COMMENT '商品名称',
`number` int NOT NULL COMMENT '库存数量',
`start_time` TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
`end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP CoMMENT '创建时间',
PRIMARY KEY (seckill_id),
KEY  idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT '秒杀库存表';

/* 初始化数据 */
insert into
  seckill(name,number,start_time,end_time)
values
('1000元秒杀iphone6',200,'2016-11-21 00:00:00','2016-11-21 00:00:00'),
('2000元秒杀iPhone7',100,'2016-11-21 00:00:00','2016-11-21 00:00:00'),
('800元秒杀三星S6',1500,'2016-11-21 00:00:00','2016-11-21 00:00:00'),
('500元秒杀小米5',300,'2016-11-21 00:00:00','2016-11-21 00:00:00');

/*秒杀成功明细*/
CREATE TABLE success_killed(
`seckill_id` BIGINT NOT NULL COMMENT '秒杀商品id',
`user_phone` BIGINT NOT NULL COMMENT '用户手机号',
`state` TINYINT NOT NULL DEFAULT -1 COMMENT '状态：-1：无效  0：成功 1：已付款',
`create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
PRIMARY KEY (seckill_id,user_phone),
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '秒杀成功明细表';

