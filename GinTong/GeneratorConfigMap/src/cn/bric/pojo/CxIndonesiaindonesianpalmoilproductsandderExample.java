package cn.bric.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CxIndonesiaindonesianpalmoilproductsandderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CxIndonesiaindonesianpalmoilproductsandderExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andEdittimeIsNull() {
            addCriterion("EditTime is null");
            return (Criteria) this;
        }

        public Criteria andEdittimeIsNotNull() {
            addCriterion("EditTime is not null");
            return (Criteria) this;
        }

        public Criteria andEdittimeEqualTo(Date value) {
            addCriterion("EditTime =", value, "edittime");
            return (Criteria) this;
        }

        public Criteria andEdittimeNotEqualTo(Date value) {
            addCriterion("EditTime <>", value, "edittime");
            return (Criteria) this;
        }

        public Criteria andEdittimeGreaterThan(Date value) {
            addCriterion("EditTime >", value, "edittime");
            return (Criteria) this;
        }

        public Criteria andEdittimeGreaterThanOrEqualTo(Date value) {
            addCriterion("EditTime >=", value, "edittime");
            return (Criteria) this;
        }

        public Criteria andEdittimeLessThan(Date value) {
            addCriterion("EditTime <", value, "edittime");
            return (Criteria) this;
        }

        public Criteria andEdittimeLessThanOrEqualTo(Date value) {
            addCriterion("EditTime <=", value, "edittime");
            return (Criteria) this;
        }

        public Criteria andEdittimeIn(List<Date> values) {
            addCriterion("EditTime in", values, "edittime");
            return (Criteria) this;
        }

        public Criteria andEdittimeNotIn(List<Date> values) {
            addCriterion("EditTime not in", values, "edittime");
            return (Criteria) this;
        }

        public Criteria andEdittimeBetween(Date value1, Date value2) {
            addCriterion("EditTime between", value1, value2, "edittime");
            return (Criteria) this;
        }

        public Criteria andEdittimeNotBetween(Date value1, Date value2) {
            addCriterion("EditTime not between", value1, value2, "edittime");
            return (Criteria) this;
        }

        public Criteria andVaridIsNull() {
            addCriterion("VarId is null");
            return (Criteria) this;
        }

        public Criteria andVaridIsNotNull() {
            addCriterion("VarId is not null");
            return (Criteria) this;
        }

        public Criteria andVaridEqualTo(Integer value) {
            addCriterion("VarId =", value, "varid");
            return (Criteria) this;
        }

        public Criteria andVaridNotEqualTo(Integer value) {
            addCriterion("VarId <>", value, "varid");
            return (Criteria) this;
        }

        public Criteria andVaridGreaterThan(Integer value) {
            addCriterion("VarId >", value, "varid");
            return (Criteria) this;
        }

        public Criteria andVaridGreaterThanOrEqualTo(Integer value) {
            addCriterion("VarId >=", value, "varid");
            return (Criteria) this;
        }

        public Criteria andVaridLessThan(Integer value) {
            addCriterion("VarId <", value, "varid");
            return (Criteria) this;
        }

        public Criteria andVaridLessThanOrEqualTo(Integer value) {
            addCriterion("VarId <=", value, "varid");
            return (Criteria) this;
        }

        public Criteria andVaridIn(List<Integer> values) {
            addCriterion("VarId in", values, "varid");
            return (Criteria) this;
        }

        public Criteria andVaridNotIn(List<Integer> values) {
            addCriterion("VarId not in", values, "varid");
            return (Criteria) this;
        }

        public Criteria andVaridBetween(Integer value1, Integer value2) {
            addCriterion("VarId between", value1, value2, "varid");
            return (Criteria) this;
        }

        public Criteria andVaridNotBetween(Integer value1, Integer value2) {
            addCriterion("VarId not between", value1, value2, "varid");
            return (Criteria) this;
        }

        public Criteria andTimeintIsNull() {
            addCriterion("TimeInt is null");
            return (Criteria) this;
        }

        public Criteria andTimeintIsNotNull() {
            addCriterion("TimeInt is not null");
            return (Criteria) this;
        }

        public Criteria andTimeintEqualTo(Integer value) {
            addCriterion("TimeInt =", value, "timeint");
            return (Criteria) this;
        }

        public Criteria andTimeintNotEqualTo(Integer value) {
            addCriterion("TimeInt <>", value, "timeint");
            return (Criteria) this;
        }

        public Criteria andTimeintGreaterThan(Integer value) {
            addCriterion("TimeInt >", value, "timeint");
            return (Criteria) this;
        }

        public Criteria andTimeintGreaterThanOrEqualTo(Integer value) {
            addCriterion("TimeInt >=", value, "timeint");
            return (Criteria) this;
        }

        public Criteria andTimeintLessThan(Integer value) {
            addCriterion("TimeInt <", value, "timeint");
            return (Criteria) this;
        }

        public Criteria andTimeintLessThanOrEqualTo(Integer value) {
            addCriterion("TimeInt <=", value, "timeint");
            return (Criteria) this;
        }

        public Criteria andTimeintIn(List<Integer> values) {
            addCriterion("TimeInt in", values, "timeint");
            return (Criteria) this;
        }

        public Criteria andTimeintNotIn(List<Integer> values) {
            addCriterion("TimeInt not in", values, "timeint");
            return (Criteria) this;
        }

        public Criteria andTimeintBetween(Integer value1, Integer value2) {
            addCriterion("TimeInt between", value1, value2, "timeint");
            return (Criteria) this;
        }

        public Criteria andTimeintNotBetween(Integer value1, Integer value2) {
            addCriterion("TimeInt not between", value1, value2, "timeint");
            return (Criteria) this;
        }

        public Criteria andNearbyIsNull() {
            addCriterion("Nearby is null");
            return (Criteria) this;
        }

        public Criteria andNearbyIsNotNull() {
            addCriterion("Nearby is not null");
            return (Criteria) this;
        }

        public Criteria andNearbyEqualTo(Double value) {
            addCriterion("Nearby =", value, "nearby");
            return (Criteria) this;
        }

        public Criteria andNearbyNotEqualTo(Double value) {
            addCriterion("Nearby <>", value, "nearby");
            return (Criteria) this;
        }

        public Criteria andNearbyGreaterThan(Double value) {
            addCriterion("Nearby >", value, "nearby");
            return (Criteria) this;
        }

        public Criteria andNearbyGreaterThanOrEqualTo(Double value) {
            addCriterion("Nearby >=", value, "nearby");
            return (Criteria) this;
        }

        public Criteria andNearbyLessThan(Double value) {
            addCriterion("Nearby <", value, "nearby");
            return (Criteria) this;
        }

        public Criteria andNearbyLessThanOrEqualTo(Double value) {
            addCriterion("Nearby <=", value, "nearby");
            return (Criteria) this;
        }

        public Criteria andNearbyIn(List<Double> values) {
            addCriterion("Nearby in", values, "nearby");
            return (Criteria) this;
        }

        public Criteria andNearbyNotIn(List<Double> values) {
            addCriterion("Nearby not in", values, "nearby");
            return (Criteria) this;
        }

        public Criteria andNearbyBetween(Double value1, Double value2) {
            addCriterion("Nearby between", value1, value2, "nearby");
            return (Criteria) this;
        }

        public Criteria andNearbyNotBetween(Double value1, Double value2) {
            addCriterion("Nearby not between", value1, value2, "nearby");
            return (Criteria) this;
        }

        public Criteria and1月IsNull() {
            addCriterion("1月 is null");
            return (Criteria) this;
        }

        public Criteria and1月IsNotNull() {
            addCriterion("1月 is not null");
            return (Criteria) this;
        }

        public Criteria and1月EqualTo(Double value) {
            addCriterion("1月 =", value, "1月");
            return (Criteria) this;
        }

        public Criteria and1月NotEqualTo(Double value) {
            addCriterion("1月 <>", value, "1月");
            return (Criteria) this;
        }

        public Criteria and1月GreaterThan(Double value) {
            addCriterion("1月 >", value, "1月");
            return (Criteria) this;
        }

        public Criteria and1月GreaterThanOrEqualTo(Double value) {
            addCriterion("1月 >=", value, "1月");
            return (Criteria) this;
        }

        public Criteria and1月LessThan(Double value) {
            addCriterion("1月 <", value, "1月");
            return (Criteria) this;
        }

        public Criteria and1月LessThanOrEqualTo(Double value) {
            addCriterion("1月 <=", value, "1月");
            return (Criteria) this;
        }

        public Criteria and1月In(List<Double> values) {
            addCriterion("1月 in", values, "1月");
            return (Criteria) this;
        }

        public Criteria and1月NotIn(List<Double> values) {
            addCriterion("1月 not in", values, "1月");
            return (Criteria) this;
        }

        public Criteria and1月Between(Double value1, Double value2) {
            addCriterion("1月 between", value1, value2, "1月");
            return (Criteria) this;
        }

        public Criteria and1月NotBetween(Double value1, Double value2) {
            addCriterion("1月 not between", value1, value2, "1月");
            return (Criteria) this;
        }

        public Criteria and2月IsNull() {
            addCriterion("2月 is null");
            return (Criteria) this;
        }

        public Criteria and2月IsNotNull() {
            addCriterion("2月 is not null");
            return (Criteria) this;
        }

        public Criteria and2月EqualTo(Double value) {
            addCriterion("2月 =", value, "2月");
            return (Criteria) this;
        }

        public Criteria and2月NotEqualTo(Double value) {
            addCriterion("2月 <>", value, "2月");
            return (Criteria) this;
        }

        public Criteria and2月GreaterThan(Double value) {
            addCriterion("2月 >", value, "2月");
            return (Criteria) this;
        }

        public Criteria and2月GreaterThanOrEqualTo(Double value) {
            addCriterion("2月 >=", value, "2月");
            return (Criteria) this;
        }

        public Criteria and2月LessThan(Double value) {
            addCriterion("2月 <", value, "2月");
            return (Criteria) this;
        }

        public Criteria and2月LessThanOrEqualTo(Double value) {
            addCriterion("2月 <=", value, "2月");
            return (Criteria) this;
        }

        public Criteria and2月In(List<Double> values) {
            addCriterion("2月 in", values, "2月");
            return (Criteria) this;
        }

        public Criteria and2月NotIn(List<Double> values) {
            addCriterion("2月 not in", values, "2月");
            return (Criteria) this;
        }

        public Criteria and2月Between(Double value1, Double value2) {
            addCriterion("2月 between", value1, value2, "2月");
            return (Criteria) this;
        }

        public Criteria and2月NotBetween(Double value1, Double value2) {
            addCriterion("2月 not between", value1, value2, "2月");
            return (Criteria) this;
        }

        public Criteria and3月IsNull() {
            addCriterion("3月 is null");
            return (Criteria) this;
        }

        public Criteria and3月IsNotNull() {
            addCriterion("3月 is not null");
            return (Criteria) this;
        }

        public Criteria and3月EqualTo(Double value) {
            addCriterion("3月 =", value, "3月");
            return (Criteria) this;
        }

        public Criteria and3月NotEqualTo(Double value) {
            addCriterion("3月 <>", value, "3月");
            return (Criteria) this;
        }

        public Criteria and3月GreaterThan(Double value) {
            addCriterion("3月 >", value, "3月");
            return (Criteria) this;
        }

        public Criteria and3月GreaterThanOrEqualTo(Double value) {
            addCriterion("3月 >=", value, "3月");
            return (Criteria) this;
        }

        public Criteria and3月LessThan(Double value) {
            addCriterion("3月 <", value, "3月");
            return (Criteria) this;
        }

        public Criteria and3月LessThanOrEqualTo(Double value) {
            addCriterion("3月 <=", value, "3月");
            return (Criteria) this;
        }

        public Criteria and3月In(List<Double> values) {
            addCriterion("3月 in", values, "3月");
            return (Criteria) this;
        }

        public Criteria and3月NotIn(List<Double> values) {
            addCriterion("3月 not in", values, "3月");
            return (Criteria) this;
        }

        public Criteria and3月Between(Double value1, Double value2) {
            addCriterion("3月 between", value1, value2, "3月");
            return (Criteria) this;
        }

        public Criteria and3月NotBetween(Double value1, Double value2) {
            addCriterion("3月 not between", value1, value2, "3月");
            return (Criteria) this;
        }

        public Criteria and4月IsNull() {
            addCriterion("4月 is null");
            return (Criteria) this;
        }

        public Criteria and4月IsNotNull() {
            addCriterion("4月 is not null");
            return (Criteria) this;
        }

        public Criteria and4月EqualTo(Double value) {
            addCriterion("4月 =", value, "4月");
            return (Criteria) this;
        }

        public Criteria and4月NotEqualTo(Double value) {
            addCriterion("4月 <>", value, "4月");
            return (Criteria) this;
        }

        public Criteria and4月GreaterThan(Double value) {
            addCriterion("4月 >", value, "4月");
            return (Criteria) this;
        }

        public Criteria and4月GreaterThanOrEqualTo(Double value) {
            addCriterion("4月 >=", value, "4月");
            return (Criteria) this;
        }

        public Criteria and4月LessThan(Double value) {
            addCriterion("4月 <", value, "4月");
            return (Criteria) this;
        }

        public Criteria and4月LessThanOrEqualTo(Double value) {
            addCriterion("4月 <=", value, "4月");
            return (Criteria) this;
        }

        public Criteria and4月In(List<Double> values) {
            addCriterion("4月 in", values, "4月");
            return (Criteria) this;
        }

        public Criteria and4月NotIn(List<Double> values) {
            addCriterion("4月 not in", values, "4月");
            return (Criteria) this;
        }

        public Criteria and4月Between(Double value1, Double value2) {
            addCriterion("4月 between", value1, value2, "4月");
            return (Criteria) this;
        }

        public Criteria and4月NotBetween(Double value1, Double value2) {
            addCriterion("4月 not between", value1, value2, "4月");
            return (Criteria) this;
        }

        public Criteria and5月IsNull() {
            addCriterion("5月 is null");
            return (Criteria) this;
        }

        public Criteria and5月IsNotNull() {
            addCriterion("5月 is not null");
            return (Criteria) this;
        }

        public Criteria and5月EqualTo(Double value) {
            addCriterion("5月 =", value, "5月");
            return (Criteria) this;
        }

        public Criteria and5月NotEqualTo(Double value) {
            addCriterion("5月 <>", value, "5月");
            return (Criteria) this;
        }

        public Criteria and5月GreaterThan(Double value) {
            addCriterion("5月 >", value, "5月");
            return (Criteria) this;
        }

        public Criteria and5月GreaterThanOrEqualTo(Double value) {
            addCriterion("5月 >=", value, "5月");
            return (Criteria) this;
        }

        public Criteria and5月LessThan(Double value) {
            addCriterion("5月 <", value, "5月");
            return (Criteria) this;
        }

        public Criteria and5月LessThanOrEqualTo(Double value) {
            addCriterion("5月 <=", value, "5月");
            return (Criteria) this;
        }

        public Criteria and5月In(List<Double> values) {
            addCriterion("5月 in", values, "5月");
            return (Criteria) this;
        }

        public Criteria and5月NotIn(List<Double> values) {
            addCriterion("5月 not in", values, "5月");
            return (Criteria) this;
        }

        public Criteria and5月Between(Double value1, Double value2) {
            addCriterion("5月 between", value1, value2, "5月");
            return (Criteria) this;
        }

        public Criteria and5月NotBetween(Double value1, Double value2) {
            addCriterion("5月 not between", value1, value2, "5月");
            return (Criteria) this;
        }

        public Criteria and6月IsNull() {
            addCriterion("6月 is null");
            return (Criteria) this;
        }

        public Criteria and6月IsNotNull() {
            addCriterion("6月 is not null");
            return (Criteria) this;
        }

        public Criteria and6月EqualTo(Double value) {
            addCriterion("6月 =", value, "6月");
            return (Criteria) this;
        }

        public Criteria and6月NotEqualTo(Double value) {
            addCriterion("6月 <>", value, "6月");
            return (Criteria) this;
        }

        public Criteria and6月GreaterThan(Double value) {
            addCriterion("6月 >", value, "6月");
            return (Criteria) this;
        }

        public Criteria and6月GreaterThanOrEqualTo(Double value) {
            addCriterion("6月 >=", value, "6月");
            return (Criteria) this;
        }

        public Criteria and6月LessThan(Double value) {
            addCriterion("6月 <", value, "6月");
            return (Criteria) this;
        }

        public Criteria and6月LessThanOrEqualTo(Double value) {
            addCriterion("6月 <=", value, "6月");
            return (Criteria) this;
        }

        public Criteria and6月In(List<Double> values) {
            addCriterion("6月 in", values, "6月");
            return (Criteria) this;
        }

        public Criteria and6月NotIn(List<Double> values) {
            addCriterion("6月 not in", values, "6月");
            return (Criteria) this;
        }

        public Criteria and6月Between(Double value1, Double value2) {
            addCriterion("6月 between", value1, value2, "6月");
            return (Criteria) this;
        }

        public Criteria and6月NotBetween(Double value1, Double value2) {
            addCriterion("6月 not between", value1, value2, "6月");
            return (Criteria) this;
        }

        public Criteria and7月IsNull() {
            addCriterion("7月 is null");
            return (Criteria) this;
        }

        public Criteria and7月IsNotNull() {
            addCriterion("7月 is not null");
            return (Criteria) this;
        }

        public Criteria and7月EqualTo(Double value) {
            addCriterion("7月 =", value, "7月");
            return (Criteria) this;
        }

        public Criteria and7月NotEqualTo(Double value) {
            addCriterion("7月 <>", value, "7月");
            return (Criteria) this;
        }

        public Criteria and7月GreaterThan(Double value) {
            addCriterion("7月 >", value, "7月");
            return (Criteria) this;
        }

        public Criteria and7月GreaterThanOrEqualTo(Double value) {
            addCriterion("7月 >=", value, "7月");
            return (Criteria) this;
        }

        public Criteria and7月LessThan(Double value) {
            addCriterion("7月 <", value, "7月");
            return (Criteria) this;
        }

        public Criteria and7月LessThanOrEqualTo(Double value) {
            addCriterion("7月 <=", value, "7月");
            return (Criteria) this;
        }

        public Criteria and7月In(List<Double> values) {
            addCriterion("7月 in", values, "7月");
            return (Criteria) this;
        }

        public Criteria and7月NotIn(List<Double> values) {
            addCriterion("7月 not in", values, "7月");
            return (Criteria) this;
        }

        public Criteria and7月Between(Double value1, Double value2) {
            addCriterion("7月 between", value1, value2, "7月");
            return (Criteria) this;
        }

        public Criteria and7月NotBetween(Double value1, Double value2) {
            addCriterion("7月 not between", value1, value2, "7月");
            return (Criteria) this;
        }

        public Criteria and8月IsNull() {
            addCriterion("8月 is null");
            return (Criteria) this;
        }

        public Criteria and8月IsNotNull() {
            addCriterion("8月 is not null");
            return (Criteria) this;
        }

        public Criteria and8月EqualTo(Double value) {
            addCriterion("8月 =", value, "8月");
            return (Criteria) this;
        }

        public Criteria and8月NotEqualTo(Double value) {
            addCriterion("8月 <>", value, "8月");
            return (Criteria) this;
        }

        public Criteria and8月GreaterThan(Double value) {
            addCriterion("8月 >", value, "8月");
            return (Criteria) this;
        }

        public Criteria and8月GreaterThanOrEqualTo(Double value) {
            addCriterion("8月 >=", value, "8月");
            return (Criteria) this;
        }

        public Criteria and8月LessThan(Double value) {
            addCriterion("8月 <", value, "8月");
            return (Criteria) this;
        }

        public Criteria and8月LessThanOrEqualTo(Double value) {
            addCriterion("8月 <=", value, "8月");
            return (Criteria) this;
        }

        public Criteria and8月In(List<Double> values) {
            addCriterion("8月 in", values, "8月");
            return (Criteria) this;
        }

        public Criteria and8月NotIn(List<Double> values) {
            addCriterion("8月 not in", values, "8月");
            return (Criteria) this;
        }

        public Criteria and8月Between(Double value1, Double value2) {
            addCriterion("8月 between", value1, value2, "8月");
            return (Criteria) this;
        }

        public Criteria and8月NotBetween(Double value1, Double value2) {
            addCriterion("8月 not between", value1, value2, "8月");
            return (Criteria) this;
        }

        public Criteria and9月IsNull() {
            addCriterion("9月 is null");
            return (Criteria) this;
        }

        public Criteria and9月IsNotNull() {
            addCriterion("9月 is not null");
            return (Criteria) this;
        }

        public Criteria and9月EqualTo(Double value) {
            addCriterion("9月 =", value, "9月");
            return (Criteria) this;
        }

        public Criteria and9月NotEqualTo(Double value) {
            addCriterion("9月 <>", value, "9月");
            return (Criteria) this;
        }

        public Criteria and9月GreaterThan(Double value) {
            addCriterion("9月 >", value, "9月");
            return (Criteria) this;
        }

        public Criteria and9月GreaterThanOrEqualTo(Double value) {
            addCriterion("9月 >=", value, "9月");
            return (Criteria) this;
        }

        public Criteria and9月LessThan(Double value) {
            addCriterion("9月 <", value, "9月");
            return (Criteria) this;
        }

        public Criteria and9月LessThanOrEqualTo(Double value) {
            addCriterion("9月 <=", value, "9月");
            return (Criteria) this;
        }

        public Criteria and9月In(List<Double> values) {
            addCriterion("9月 in", values, "9月");
            return (Criteria) this;
        }

        public Criteria and9月NotIn(List<Double> values) {
            addCriterion("9月 not in", values, "9月");
            return (Criteria) this;
        }

        public Criteria and9月Between(Double value1, Double value2) {
            addCriterion("9月 between", value1, value2, "9月");
            return (Criteria) this;
        }

        public Criteria and9月NotBetween(Double value1, Double value2) {
            addCriterion("9月 not between", value1, value2, "9月");
            return (Criteria) this;
        }

        public Criteria and10月IsNull() {
            addCriterion("10月 is null");
            return (Criteria) this;
        }

        public Criteria and10月IsNotNull() {
            addCriterion("10月 is not null");
            return (Criteria) this;
        }

        public Criteria and10月EqualTo(Double value) {
            addCriterion("10月 =", value, "10月");
            return (Criteria) this;
        }

        public Criteria and10月NotEqualTo(Double value) {
            addCriterion("10月 <>", value, "10月");
            return (Criteria) this;
        }

        public Criteria and10月GreaterThan(Double value) {
            addCriterion("10月 >", value, "10月");
            return (Criteria) this;
        }

        public Criteria and10月GreaterThanOrEqualTo(Double value) {
            addCriterion("10月 >=", value, "10月");
            return (Criteria) this;
        }

        public Criteria and10月LessThan(Double value) {
            addCriterion("10月 <", value, "10月");
            return (Criteria) this;
        }

        public Criteria and10月LessThanOrEqualTo(Double value) {
            addCriterion("10月 <=", value, "10月");
            return (Criteria) this;
        }

        public Criteria and10月In(List<Double> values) {
            addCriterion("10月 in", values, "10月");
            return (Criteria) this;
        }

        public Criteria and10月NotIn(List<Double> values) {
            addCriterion("10月 not in", values, "10月");
            return (Criteria) this;
        }

        public Criteria and10月Between(Double value1, Double value2) {
            addCriterion("10月 between", value1, value2, "10月");
            return (Criteria) this;
        }

        public Criteria and10月NotBetween(Double value1, Double value2) {
            addCriterion("10月 not between", value1, value2, "10月");
            return (Criteria) this;
        }

        public Criteria and11月IsNull() {
            addCriterion("11月 is null");
            return (Criteria) this;
        }

        public Criteria and11月IsNotNull() {
            addCriterion("11月 is not null");
            return (Criteria) this;
        }

        public Criteria and11月EqualTo(Double value) {
            addCriterion("11月 =", value, "11月");
            return (Criteria) this;
        }

        public Criteria and11月NotEqualTo(Double value) {
            addCriterion("11月 <>", value, "11月");
            return (Criteria) this;
        }

        public Criteria and11月GreaterThan(Double value) {
            addCriterion("11月 >", value, "11月");
            return (Criteria) this;
        }

        public Criteria and11月GreaterThanOrEqualTo(Double value) {
            addCriterion("11月 >=", value, "11月");
            return (Criteria) this;
        }

        public Criteria and11月LessThan(Double value) {
            addCriterion("11月 <", value, "11月");
            return (Criteria) this;
        }

        public Criteria and11月LessThanOrEqualTo(Double value) {
            addCriterion("11月 <=", value, "11月");
            return (Criteria) this;
        }

        public Criteria and11月In(List<Double> values) {
            addCriterion("11月 in", values, "11月");
            return (Criteria) this;
        }

        public Criteria and11月NotIn(List<Double> values) {
            addCriterion("11月 not in", values, "11月");
            return (Criteria) this;
        }

        public Criteria and11月Between(Double value1, Double value2) {
            addCriterion("11月 between", value1, value2, "11月");
            return (Criteria) this;
        }

        public Criteria and11月NotBetween(Double value1, Double value2) {
            addCriterion("11月 not between", value1, value2, "11月");
            return (Criteria) this;
        }

        public Criteria and12月IsNull() {
            addCriterion("12月 is null");
            return (Criteria) this;
        }

        public Criteria and12月IsNotNull() {
            addCriterion("12月 is not null");
            return (Criteria) this;
        }

        public Criteria and12月EqualTo(Double value) {
            addCriterion("12月 =", value, "12月");
            return (Criteria) this;
        }

        public Criteria and12月NotEqualTo(Double value) {
            addCriterion("12月 <>", value, "12月");
            return (Criteria) this;
        }

        public Criteria and12月GreaterThan(Double value) {
            addCriterion("12月 >", value, "12月");
            return (Criteria) this;
        }

        public Criteria and12月GreaterThanOrEqualTo(Double value) {
            addCriterion("12月 >=", value, "12月");
            return (Criteria) this;
        }

        public Criteria and12月LessThan(Double value) {
            addCriterion("12月 <", value, "12月");
            return (Criteria) this;
        }

        public Criteria and12月LessThanOrEqualTo(Double value) {
            addCriterion("12月 <=", value, "12月");
            return (Criteria) this;
        }

        public Criteria and12月In(List<Double> values) {
            addCriterion("12月 in", values, "12月");
            return (Criteria) this;
        }

        public Criteria and12月NotIn(List<Double> values) {
            addCriterion("12月 not in", values, "12月");
            return (Criteria) this;
        }

        public Criteria and12月Between(Double value1, Double value2) {
            addCriterion("12月 between", value1, value2, "12月");
            return (Criteria) this;
        }

        public Criteria and12月NotBetween(Double value1, Double value2) {
            addCriterion("12月 not between", value1, value2, "12月");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}