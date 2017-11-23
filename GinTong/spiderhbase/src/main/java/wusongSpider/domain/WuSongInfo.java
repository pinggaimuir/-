package wusongSpider.domain;

/**
 * Created by 高健 on 2017/3/10.
 */
public class WuSongInfo {
    private String title;//获取标题
    private String court;//法院
    private String trialRound;//几审
    private String caseNumber;//caseNumber
    private String keyWord;//关键词
    private String sourceName;//来源
    private String issueNumber;//期刊号
    private String judgementAbstract;//裁判摘要
    private String regulationItem;//引用法规
    private String litigant;//当事人信息
    private String afterTheTrial;//审理经过
    private String courtIdentified;//一审法院查明
    private String plaintiffSaid;//原告诉称
    private String argument;//被上诉人辩称
    private String hasIdentified;//本院查明
    private String courtThink;//本院认为
    private String secondResult;//二审裁判结果
    private String theJudge;//审判人员
    private String refereeDate;//裁判日期
    private String clerk;//书记员
    private String evaluation;//案例自评

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getTrialRound() {
        return trialRound;
    }

    public void setTrialRound(String trialRound) {
        this.trialRound = trialRound;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getJudgementAbstract() {
        return judgementAbstract;
    }

    public void setJudgementAbstract(String judgementAbstract) {
        this.judgementAbstract = judgementAbstract;
    }

    public String getRegulationItem() {
        return regulationItem;
    }

    public void setRegulationItem(String regulationItem) {
        this.regulationItem = regulationItem;
    }

    public String getLitigant() {
        return litigant;
    }

    public void setLitigant(String litigant) {
        this.litigant = litigant;
    }

    public String getPlaintiffSaid() {
        return plaintiffSaid;
    }

    public void setPlaintiffSaid(String plaintiffSaid) {
        this.plaintiffSaid = plaintiffSaid;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getAfterTheTrial() {
        return afterTheTrial;
    }

    public void setAfterTheTrial(String afterTheTrial) {
        this.afterTheTrial = afterTheTrial;
    }

    public String getCourtIdentified() {
        return courtIdentified;
    }

    public void setCourtIdentified(String courtIdentified) {
        this.courtIdentified = courtIdentified;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String getHasIdentified() {
        return hasIdentified;
    }

    public void setHasIdentified(String hasIdentified) {
        this.hasIdentified = hasIdentified;
    }

    public String getCourtThink() {
        return courtThink;
    }

    public void setCourtThink(String courtThink) {
        this.courtThink = courtThink;
    }

    public String getSecondResult() {
        return secondResult;
    }

    public void setSecondResult(String secondResult) {
        this.secondResult = secondResult;
    }

    public String getTheJudge() {
        return theJudge;
    }

    public void setTheJudge(String theJudge) {
        this.theJudge = theJudge;
    }

    public String getRefereeDate() {
        return refereeDate;
    }

    public void setRefereeDate(String refereeDate) {
        this.refereeDate = refereeDate;
    }

    public String getClerk() {
        return clerk;
    }

    public void setClerk(String clerk) {
        this.clerk = clerk;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        return "WuSongInfo{" +
                "title='" + title + '\'' +
                ", court='" + court + '\'' +
                ", trialRound='" + trialRound + '\'' +
                ", caseNumber='" + caseNumber + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", issueNumber='" + issueNumber + '\'' +
                ", judgementAbstract='" + judgementAbstract + '\'' +
                ", regulationItem='" + regulationItem + '\'' +
                ", litigant='" + litigant + '\'' +
                ", afterTheTrial='" + afterTheTrial + '\'' +
                ", courtIdentified='" + courtIdentified + '\'' +
                ", argument='" + argument + '\'' +
                ", hasIdentified='" + hasIdentified + '\'' +
                ", courtThink='" + courtThink + '\'' +
                ", secondResult='" + secondResult + '\'' +
                ", theJudge='" + theJudge + '\'' +
                ", refereeDate='" + refereeDate + '\'' +
                ", clerk='" + clerk + '\'' +
                ", evaluation='" + evaluation + '\'' +
                '}';
    }
}
