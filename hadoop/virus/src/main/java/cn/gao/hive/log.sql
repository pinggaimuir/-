/*pv*/
SELECT count(*) from flux where reportTime='2017-01-08';
/*uv*/
SELECT count(DISTINCT stat_ss) AS uv from flux where reportTime='2017-01-08';
/**/