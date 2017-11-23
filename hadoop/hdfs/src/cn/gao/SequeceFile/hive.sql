
-- pv
select count * fom flux where reportTime='2017-01-08'
-- uv
select count(distinct statuv) from flux where reportTime='2017-01-08'
-- vv
select count(distinct split(stat_ss,"_")[0]) from flux where reportTime='2017-01-08'
-- newip
select count(distinct cip) from flux where
  reportTime='2017-01-08'
  and cip not in (select cip as ip from flux where reportTime <> '2017-01-08')
-- 跳出率br
select count(*) from flux where reportTime='2017-01-08'

