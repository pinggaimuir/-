
/*#建立数据库*/
create database flux;

/*#建立外部表*/
create EXTERNAL table flux (host string,url String,urlname String,title String,chset String,scr String,col String,lg String,je String,ec String,fv String,cn String,ref String,uagent String,stat_ss String,stat_uv String,cip String) partitioned by (reportTime string) row format delimited fields terminated by '|' stored as textfile location '/flux';

/*#增加分区*/
ALTER TABLE flux add  PARTITION (reportTime='2016-12-02') location '/flux/reportTime=2016-12-02';

/*网站访问次数pv*/
select count(*) as pv from  flux where reportTime='2017-01-09';

/*uv*/
select count(distinct stat_ss) as uv from  flux where reportTime='2017-01-09';

/*vv*/
select count(DISTINCT split(stat_uv,'_')[0]) as vv from flux where reportTime='2017-01-09';

/*跳出率*/
select round(a/b,2) from (select count(*)as a from (select count(distinct urlname) as u from flux group by split(stat_uv,'_')[0]) as c where u==1) as tab1 left join (select count(DISTINCT split(stat_uv,'_')[0]) as b from flux) as tab2;

/*新增独立ip*/
select count(DISTINCT cip) from flux where reportTime='2017-01-09' and cip not in (select distinct cip as ip from (select * from flux) as newt where reportTime <> '2017-01-09');

/*平均时长avgtime*/
select round(avg(tim),2) as avgtime from (select round((max(split(stat_ss,'_')[2])-min(split(stat_ss,'_')[2]))/1000,2) as tim from flux group by split(stat_ss,'_')[0]) as timet;

/*新增独立访客newcust*/
select count(distinct stat_ss) as newcust from flux where stat_ss not in (select distinct stat_ss as history from (select * from flux) as tnewcust where reportTime<>'2016-12-02') and reportTime='2016-12-02';

/*访问深度wiewdeep*/
select round(avg(deep),2) as wd from (select count(distinct urlname) as deep from flux group by split(stat_ss,'_')[0]) as tt;

/*创建结果表*/
create table tongji(time date,pv int,uv int,vv int,br double,newip int,avgtime int,newcust int,viewdeep int)row format delimited fields terminated by '|';
/*把计算结果合并插入表中*/
insert overwrite table tongji select '2016-12-02',t1.pv,t2.uv,t3.vv,t4.br,t5.newip,t6.avgtime,t7.newcust,t8.viewdeep from (select count(*) as pv from  flux where reportTime='2016-12-02' ) as t1 left join (select count(distinct stat_ss) as uv from flux where reportTime='2016-12-02') as t2 left join (select count(distinct split(stat_uv,'_')[0]) as vv from flux where reportTime='2016-12-02') as t3 left join (select round(x/y,2) as br from (select count(*) as x from (select count(*) as countView from flux group by split(stat_uv,'_')[0] having countView = 1) as tbr11) as tbr1 left join (select count(*) as y from (select count(*) as countView from flux group by split(stat_uv,'_')[0]) as tbr22) as tbr2) as t4 left join (select count(distinct cip) as newip from flux where cip not in (select distinct cip as history from (select * from flux) as tnewip where reportTime<>'2016-12-02') and reportTime='2016-12-02') as t5 left join (select round(avg(time),2) as avgtime from(select round((max(split(stat_uv,'_')[2]) - min(split(stat_uv,'_')[2]))/1000,0) time from flux group by split(stat_uv,'_')[0]) as tavgtime) as t6 left join (select count(distinct stat_ss) as newcust from flux where stat_ss not in (select distinct stat_ss as history from (select * from flux) as tnewcust where reportTime<>'2016-12-02') and reportTime='2016-12-02') as t7 left join(select round(avg(deep),2) as viewdeep from (select count(distinct urlname) as deep from flux where reportTime='2016-12-02' group by split(stat_uv,'_')[0]) as tviewdeep) as t8;


