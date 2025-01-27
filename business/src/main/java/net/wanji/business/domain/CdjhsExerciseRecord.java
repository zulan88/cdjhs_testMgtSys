package net.wanji.business.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.wanji.common.annotation.Excel;
import net.wanji.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 练习记录对象 cdjhs_exercise_record
 * 
 * @author ruoyi
 * @date 2024-06-19
 */
public class CdjhsExerciseRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 练习id */
    private Long id;

    //测试用例id
    private Long testId;

    /** 测试用例编号 */
    @Excel(name = "测试用例编号")
    private String testCaseCode;

    /** 测试用例名称 */
    @Excel(name = "测试用例名称")
    private String testCaseName;

    /** 所属试卷类型 1: A卷 2: B卷 3: C卷 */
    @Excel(name = "所属试卷类型 1: A卷 2: B卷 3: C卷")
    private Integer testPaperType;

    //测试场地
    private String testPlace;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    //团队名称
    private String teamName;

    /** 镜像名称 */
    @Excel(name = "镜像名称")
    private String mirrorName;

    /** 镜像版本 */
    @Excel(name = "镜像版本")
    private String mirrorVersion;

    /** 镜像id */
    @Excel(name = "镜像id")
    private String mirrorId;

    /** 镜像本地地址 */
    @Excel(name = "镜像本地地址")
    private String mirrorPath;

    private String md5;

    //实车编号
    private String carCode;

    /** 练习设备唯一标识 */
    @Excel(name = "练习设备唯一标识")
    private String deviceId;

    /** 练习状态 1: 待开始 2: 进行中 3: 已完成 */
    @Excel(name = "练习状态 1: 待开始 2: 进行中 3: 已完成")
    private Integer status;

    /** 前方排队 */
    @Excel(name = "前方排队")
    private Integer waitingNum;

    /** 校验结果 0: 成功 1: 失败 */
    @Excel(name = "校验结果 0: 成功 1: 失败")
    private Integer checkResult;

    /** 校验结果说明 当校验失败时需要显示 */
    @Excel(name = "校验结果说明 当校验失败时需要显示")
    private String checkMsg;

    /** 练习开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "练习开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 练习结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "练习结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 测试总时长 */
    @Excel(name = "测试总时长")
    private String duration;

    /** 评分 */
    @Excel(name = "评分")
    private Double score;

    private Double subScore;

    private Double totalScore;

    private Integer isCollect;

    //是否是比赛任务 0:否 1:是 默认值0
    private Integer isCompetition;

    private String taskCaseVos;

    private String fusionFilePath;

    private String evaluationOutput;

    private String networkId;

    private String evaluationUrl;

    private String evaluationTaskStatus;

    private Long teamId;

    private Long userId;

    public Long getTeamId() {
        return teamId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public void setTestCaseCode(String testCaseCode)
    {
        this.testCaseCode = testCaseCode;
    }

    public String getTestCaseCode() 
    {
        return testCaseCode;
    }
    public void setTestCaseName(String testCaseName) 
    {
        this.testCaseName = testCaseName;
    }

    public String getTestCaseName() 
    {
        return testCaseName;
    }
    public void setTestPaperType(Integer testPaperType) 
    {
        this.testPaperType = testPaperType;
    }

    public Integer getTestPaperType() 
    {
        return testPaperType;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setMirrorName(String mirrorName) 
    {
        this.mirrorName = mirrorName;
    }

    public String getMirrorName() 
    {
        return mirrorName;
    }
    public void setMirrorVersion(String mirrorVersion) 
    {
        this.mirrorVersion = mirrorVersion;
    }

    public String getMirrorVersion() 
    {
        return mirrorVersion;
    }
    public void setMirrorId(String mirrorId) 
    {
        this.mirrorId = mirrorId;
    }

    public String getMirrorId() 
    {
        return mirrorId;
    }
    public void setMirrorPath(String mirrorPath) 
    {
        this.mirrorPath = mirrorPath;
    }

    public String getMirrorPath() 
    {
        return mirrorPath;
    }
    public void setDeviceId(String deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId() 
    {
        return deviceId;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setWaitingNum(Integer waitingNum) 
    {
        this.waitingNum = waitingNum;
    }

    public Integer getWaitingNum() 
    {
        return waitingNum;
    }
    public void setCheckResult(Integer checkResult) 
    {
        this.checkResult = checkResult;
    }

    public Integer getCheckResult() 
    {
        return checkResult;
    }
    public void setCheckMsg(String checkMsg) 
    {
        this.checkMsg = checkMsg;
    }

    public String getCheckMsg() 
    {
        return checkMsg;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setDuration(String duration) 
    {
        this.duration = duration;
    }

    public String getDuration() 
    {
        return duration;
    }
    public void setScore(Double score)
    {
        this.score = score;
    }

    public Double getScore()
    {
        return score;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getTaskCaseVos() {
        return taskCaseVos;
    }

    public void setTaskCaseVos(String taskCaseVos) {
        this.taskCaseVos = taskCaseVos;
    }

    public String getFusionFilePath() {
        return fusionFilePath;
    }

    public void setFusionFilePath(String fusionFilePath) {
        this.fusionFilePath = fusionFilePath;
    }

    public String getEvaluationOutput() {
        return evaluationOutput;
    }

    public void setEvaluationOutput(String evaluationOutput) {
        this.evaluationOutput = evaluationOutput;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getEvaluationUrl() {
        return evaluationUrl;
    }

    public void setEvaluationUrl(String evaluationUrl) {
        this.evaluationUrl = evaluationUrl;
    }

    public String getEvaluationTaskStatus() {
        return evaluationTaskStatus;
    }

    public void setEvaluationTaskStatus(String evaluationTaskStatus) {
        this.evaluationTaskStatus = evaluationTaskStatus;
    }

    public String getTestPlace() {
        return testPlace;
    }

    public void setTestPlace(String testPlace) {
        this.testPlace = testPlace;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCarCode() {
        return carCode;
    }

    public void setCarCode(String carCode) {
        this.carCode = carCode;
    }

    public Integer getIsCompetition() {
        return isCompetition;
    }

    public void setIsCompetition(Integer isCompetition) {
        this.isCompetition = isCompetition;
    }

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Double getSubScore() {
        return subScore;
    }

    public void setSubScore(Double subScore) {
        this.subScore = subScore;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("testCaseCode", getTestCaseCode())
            .append("testCaseName", getTestCaseName())
            .append("testPaperType", getTestPaperType())
            .append("userName", getUserName())
            .append("mirrorName", getMirrorName())
            .append("mirrorVersion", getMirrorVersion())
            .append("mirrorId", getMirrorId())
            .append("mirrorPath", getMirrorPath())
            .append("deviceId", getDeviceId())
            .append("status", getStatus())
            .append("waitingNum", getWaitingNum())
            .append("checkResult", getCheckResult())
            .append("checkMsg", getCheckMsg())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("duration", getDuration())
            .append("score", getScore())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
