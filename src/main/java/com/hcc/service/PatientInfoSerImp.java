package com.hcc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcc.mapper.*;
import com.hcc.pojo.*;
import com.hcc.util.*;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatientInfoSerImp implements PatientInfoService {

    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Autowired
    private StaffDepartmentsMapper staffDepartmentsMapper;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private RecordCategoryMapper recordCategoryMapper;

    @Autowired
    private PatientDescribeMapper patientDescribeMapper;

    @Autowired
    private StaffInfoMapper staffInfoMapper;

    @Autowired
    private MedicineFrequencyMapper medicineFrequencyMapper;

    @Autowired
    private WPrescribeMapper wPrescribeMapper;

    @Autowired
    private CPrescribeMapper cPrescribeMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private WPreBatchMapper wPreBatchMapper;

    @Autowired
    private CPreBatchMapper cPreBatchMapper;

    @Autowired
    private ProBatchMapper proBatchMapper;

    // 导入ES处理接口
    @Autowired
    private PatientDesEs patientDesEs;

    @Autowired
    private ClinicDiagnosisEs clinicDiagnosisEs;

    @Autowired
    private MedicineItemEs medicineItemEs;

    @Autowired
    private WDirectionEs wDirectionEs;

    @Autowired
    private CDirectionEs cDirectionEs;

    @Autowired
    private ProjectItemEs projectItemEs;







    // 检索患者信息
    @Override
    public PageInfo<PatientInfo> selectPatientInfo(Integer page, Integer limit, PatientInfo patientInfo) {

        List<PatientInfo> patientInfos=new ArrayList<>();
        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        // 如果就诊卡，姓名，电话全都为空，就不能加载数据

        if((patientInfo.getPatientId()==null ||patientInfo.getPatientId().equals("")) && (patientInfo.getPatientName()==null || patientInfo.getPatientName().equals("")) && (patientInfo.getPhone()==null || patientInfo.getPhone().equals(""))){

        }else {
            patientInfos=patientInfoMapper.selectPatient(patientInfo);
        }

        PageInfo<PatientInfo> patientInfoPageInfo=new PageInfo<>(patientInfos); // 使用pageinfo，对上面查询的数据进行分装

        return patientInfoPageInfo;
    }

    // 患者登记
    @Override
    public boolean savePatientInfo(PatientInfo patientInfo) {

        int res=patientInfoMapper.savePatient(patientInfo);
        if(res==1){
            return true;
        }else {
            return false;
        }

    }

    // 查询挂号科室
    @Override
    public List<StaffDepartments> selectOutDpart() {
        List<StaffDepartments> staffDepartments=staffDepartmentsMapper.selectOutDepart();
        return staffDepartments;
    }


    // 查询挂号类型
    @Override
    public List<RecordCategory> selectRecCat() {

        List<RecordCategory> recordCategories=recordCategoryMapper.selectAll();
        return recordCategories;
    }

    // 保存患者挂号信息
    @Override
    public boolean saveReg(StaffInfo staffInfo, PatientInfo patientInfo,
                           String registerCategory , MedicalRecord medicalRecord) {
        // 封装信息
        medicalRecord.setPatientInfo(patientInfo);
        medicalRecord.setStaffInfo(staffInfo);
        String[] res=registerCategory.split(" ");
        medicalRecord.setRegisterCategory(res[0]);
        medicalRecord.setRegisterPrice(Double.parseDouble(res[2]));
        medicalRecord.setRegisterStatus("已挂号");

        int re=medicalRecordMapper.saveReg(medicalRecord);
        if(re==0){
            return false;
        }else {
            return true;
        }

    }

    // 今日候诊--默认
    @Override
    public PageInfo<MedicalRecord> showListDefault(Integer page, Integer limit) {

        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        List<MedicalRecord> medicalRecords=medicalRecordMapper.showWaitListDefault();
        PageInfo<MedicalRecord> medicalRecordPageInfo=new PageInfo<>(medicalRecords);
        return medicalRecordPageInfo;
    }

    // 今日候诊--查询
    @Override
    public PageInfo<MedicalRecord> showList(Integer page, Integer limit, StaffInfo staffInfo, StaffDepartments staffDepartments, PatientInfo patientInfo, MedicalRecord medicalRecord) {

        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        staffInfo.setStaffDepartment(staffDepartments); // 存入科室选择
        medicalRecord.setStaffInfo(staffInfo); // 存入员工选择
        medicalRecord.setPatientInfo(patientInfo); // 存入患者信息
        List<MedicalRecord> medicalRecords=medicalRecordMapper.showWaitList(medicalRecord);
        PageInfo<MedicalRecord> medicalRecordPageInfo=new PageInfo<>(medicalRecords);

        return medicalRecordPageInfo;
    }

    // 获取患者信息--患者预诊界面使用
    @Override
    public Map<String, Object> showPrePatient(MedicalRecord medicalRecord) {

        Map<String,Object> result=new HashMap<>();

        // 读取病历表
        medicalRecord=medicalRecordMapper.showMedicalForPre(medicalRecord);
        BigInteger patientId=medicalRecord.getPatientInfo().getPatientId(); // 读取患者id
        // 根据病历里的患者id，读取患者信息表
        PatientInfo patientInfo=new PatientInfo();
        patientInfo.setPatientId(patientId);
        List<PatientInfo> patientInfos=patientInfoMapper.selectPatient(patientInfo);

        // 读取医生表
        BigInteger staffInfoId=medicalRecord.getStaffInfo().getStaffId();
        List<StaffInfo> staffInfos=staffInfoMapper.selectStaffId(staffInfoId);


        result.put("medicalRecored",medicalRecord);
        result.put("PatientInfo",patientInfos);
        result.put("staffInfos",staffInfos);

        return result;
    }

    // 获取患者主诉列表(搭配ES进行搜索)
    @Override
    //public PageInfo<PatientDescribe> showPatientDes(Integer page, Integer limit, String str) {
    public Page<PatientDescribe> showPatientDes(Integer page, Integer limit, String str) {
        // 简码转换
        PinYinTrans pinYinTrans =new PinYinTransImpl();
        String code=pinYinTrans.transToFirst(str);

        // mysql模糊搜索方法
        //PatientDescribe patientDescribe=new PatientDescribe();
        //patientDescribe.setPatientDescribeCode(code);
        //PageHelper.startPage(page,limit);
        //List<PatientDescribe> patientDescribes=patientDescribeMapper.showPatientDes(patientDescribe);
        //PageInfo<PatientDescribe> describePageInfo=new PageInfo<>(patientDescribes);
        //return describePageInfo;

        // 启用ES搜索
        // 构建分词查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 构建查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("patientDescribeCode",code));
        // 分页查询，默认第一页为0
        queryBuilder.withPageable(PageRequest.of(page-1,limit));

        Page<PatientDescribe> patientDescribes=patientDesEs.search(queryBuilder.build());

        return patientDescribes;


    }


    // 更新病历
    @Override
    public boolean updatePreMedicalRecord(MedicalRecord medicalRecord) {

        int flag=medicalRecordMapper.updatePreMedicalRe(medicalRecord);
        if(flag==1){
            return true;
        }else {
            return false;
        }

    }

    // 更新患者信息
    @Override
    public boolean updatePrePatientInfo(PatientInfo patientInfo) {

        int flag=patientInfoMapper.updatePrePatient(patientInfo);
        if(flag==1){
            return true;
        }else {
            return false;
        }

    }

    // 检索临床诊断（ES）
    @Override
    public Page<ClinicDiagnosis> showClinicDia(Integer page, Integer limit, String str) {
        // 简码转换
        PinYinTrans pinYinTrans =new PinYinTransImpl();
        String code=pinYinTrans.transToFirst(str);

        // 启用ES搜索
        // 构建分词查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 构建查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("clinicDiagnosisCode",code));
        // 分页查询，默认第一页为0
        queryBuilder.withPageable(PageRequest.of(page-1,limit));

        Page<ClinicDiagnosis> clinicDiagnoses=clinicDiagnosisEs.search(queryBuilder.build());

        return clinicDiagnoses;


    }

    // 检索在售的药品条目
    @Override
    public Page<MedicineItem> showMedicineItemEs(Integer page, Integer limit, String str, String group) {

        // 简码转换
        PinYinTrans pinYinTrans =new PinYinTransImpl();
        String code=pinYinTrans.transToFirst(str);

        // 启用ES搜索
        // 构建分词查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();


        // 判断搜索的药品类型
        switch (group){
            case "wPrescribe":{ // 西药（西药，中成药，生物制品）
                // 构建查询条件
                queryBuilder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("medicinesCode",code)).must(QueryBuilders.termsQuery("medicinesCategory","西药","中成药","生物制品")));
                break;
            }
            case "cPrescribe":{ // 中药材
                queryBuilder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("medicinesCode",code)).must(QueryBuilders.termsQuery("medicinesCategory","中药材")));
                break;
            }

        }
        queryBuilder.withPageable(PageRequest.of(page-1,limit));

        Page<MedicineItem> medicineItems=medicineItemEs.search(queryBuilder.build());

        return medicineItems;


    }

    // 检索西药用法（Es）
    @Override
    public Page<WDirection> showWDirectionEs(Integer page, Integer limit, String str) {

        // 简码转换
        PinYinTrans pinYinTrans =new PinYinTransImpl();
        String code=pinYinTrans.transToFirst(str);

        // 启用ES搜索
        // 构建分词查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("wDirectionCode",code));
        queryBuilder.withPageable(PageRequest.of(page-1,limit));
        Page<WDirection> wDirections=wDirectionEs.search(queryBuilder.build());

        return wDirections;
    }

    // 检索用药频次表
    @Override
    public PageInfo<MedicineFrequency> showMedicineFrequency(Integer page, Integer limit, MedicineFrequency medicineFrequency) {


        // 转换拼音简码
        PinYinTrans pinYinTrans =new PinYinTransImpl();
        if(medicineFrequency.getMedicineFrequencyCode()!=null){
            medicineFrequency.setMedicineFrequencyCode(pinYinTrans.transToFirst(medicineFrequency.getMedicineFrequencyCode()));
        }else if(medicineFrequency.getMedicineFrequencyText()!=null){
            medicineFrequency.setMedicineFrequencyCode(pinYinTrans.transToFirst(medicineFrequency.getMedicineFrequencyText()));
        }
        medicineFrequency.setFrequencyStatus("1");

        // 检索数据
        // 分装查询数据
        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        List<MedicineFrequency> medicineFrequencies=medicineFrequencyMapper.searchFrequency(medicineFrequency);
        PageInfo<MedicineFrequency> medicineFrequencyPageInfo =new PageInfo<>(medicineFrequencies);
        return medicineFrequencyPageInfo;
    }


    // 检索中药用法（Es）
    @Override
    public Page<CDirection> showCDirectionEs(Integer page, Integer limit, String str) {

        // 简码转换
        PinYinTrans pinYinTrans =new PinYinTransImpl();
        String code=pinYinTrans.transToFirst(str);

        // 启用ES搜索
        // 构建分词查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("cDirectionCode",code));
        queryBuilder.withPageable(PageRequest.of(page-1,limit));
        Page<CDirection> cDirections=cDirectionEs.search(queryBuilder.build());

        return cDirections;
    }

    // 检索治疗单（ES）
    @Override
    public Page<ProjectItem> showProjectItemEs(Integer page, Integer limit, String str) {
        // 简码转换
        PinYinTrans pinYinTrans =new PinYinTransImpl();
        String code=pinYinTrans.transToFirst(str);

        // 启用ES搜索
        // 构建分词查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("projectCode",code));
        queryBuilder.withPageable(PageRequest.of(page-1,limit));
        Page<ProjectItem> projectItems=projectItemEs.search(queryBuilder.build());

        return projectItems;
    }

    @Override
    public boolean saveAllDoctorPrescribe(MedicalRecord medicalRecord, PatientInfo patientInfo, List<WPrescribe> wPrescribes, List<CPrescribe> cPrescribes, List<Project> projects) {

        //病历表标记，判断是否需要缴费
        if(wPrescribes.size()==0 && cPrescribes.size()==0 && projects.size()==0){
            // 此时无需缴费
            medicalRecord.setIsNeed(0); // 无需缴费
            medicalRecord.setIsReturn(0); // 未退费
            medicalRecord.setIsSend(0); // 未缴费
            medicalRecord.setRegisterStatus("已接诊");

            // 更新病历（）
            int mrNum=medicalRecordMapper.updateAftMedicalRe(medicalRecord);

            // 更新患者信息表（和预诊一个模式）
            int patNum=patientInfoMapper.updatePrePatient(patientInfo);

            if(mrNum==1 && patNum==1){
                return true;
            }else {
                return false;
            }

        }else {
            medicalRecord.setIsNeed(1); // 需要缴费
            medicalRecord.setIsReturn(0); // 未退费
            medicalRecord.setIsSend(0); // 未缴费
            medicalRecord.setRegisterStatus("已接诊");

            // 更新病历表
            int mrNum=medicalRecordMapper.updateAftMedicalRe(medicalRecord);
            // 更新患者信息表
            int patNum=patientInfoMapper.updatePrePatient(patientInfo);
            //System.out.println(medicalRecord);
            int wp=0;
            int cp=0;
            int po=0;
            int howmany=0;
            // 判断每种处方单
            if(wPrescribes.size()!=0){ // 西药处方单设置并存储

                for (WPrescribe wPrescribe:wPrescribes){
                    wPrescribe.setIsSend(0);
                    wPrescribe.setIsReturn(0);
                    wPrescribe.setMedicalRecord(medicalRecord);
                    wPrescribe.setwPrescribeStatus("1");
                    howmany++;
                }
                //System.out.println(wPrescribes);
                // 存储西药处方，批量模式
                wp=wPrescribeMapper.insertAllWP(wPrescribes);
            }

            if(cPrescribes.size()!=0){ // 中药处方单设置并存储

                for (CPrescribe cPrescribe:cPrescribes){
                    howmany++;
                    cPrescribe.setIsSend(0);
                    cPrescribe.setIsReturn(0);
                    cPrescribe.setMedicalRecord(medicalRecord);
                    cPrescribe.setcPrescribeStatus("1");
                }
                //System.out.println(cPrescribes);
                // 存储中药处方，批量模式
                cp=cPrescribeMapper.saveAllCP(cPrescribes);
            }

            if(projects.size()!=0){ // 中药处方单设置并存储

                for (Project project:projects){
                    project.setIsSend(0);
                    project.setIsReturn(0);
                    project.setMedicalRecord(medicalRecord);
                    project.setProjectStatus("1");
                    howmany++;
                }
                //System.out.println(projects);
                // 存储治疗处方，批量模式
               po=projectMapper.insertAllPro(projects);
            }
            int sum=wp+cp+po;

            if(sum == howmany){ // 判断是否插入成功
                return true;
            }else {
                return false;
            }
        }

    }

    // 获取待收费列表
    @Override
    public PageInfo<MedicalRecord> showWaitCharge(Integer page, Integer limit, PatientInfo patientInfo) {

        MedicalRecord medicalRecord=new MedicalRecord();
        medicalRecord.setPatientInfo(patientInfo);
        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        List<MedicalRecord> medicalRecords=medicalRecordMapper.showWaitCharge(medicalRecord);
        PageInfo<MedicalRecord> medicalRecordsPageInfo =new PageInfo<>(medicalRecords);
        return medicalRecordsPageInfo;

    }


    // 收费界面获取西药处方
    @Override
    public List<WPrescribe> showWaitWP(String medicialRecordId) {

        List<WPrescribe> wPrescribes=wPrescribeMapper.selectWPByMrId(medicialRecordId);

        return wPrescribes;
    }

    // 收费界面获取中药处方
    @Override
    public List<CPrescribe> showWaitCP(String medicialRecordId) {

        List<CPrescribe> cPrescribes=cPrescribeMapper.selectCPByMrId(medicialRecordId);

        return cPrescribes;
    }

    // 收费界面获取治疗单
    @Override
    public List<Project> showWaitPro(String medicialRecordId) {

        List<Project> projects=projectMapper.selectProByMrId(medicialRecordId);
        return projects;
    }

    // 窗口缴费确认
    @Override
    public int chargeMedicalRe(MedicalRecord medicalRecord) {

        // 修改缴费标志
        medicalRecord.setIsSend(1); // 已缴费
        medicalRecord.setRegisterStatus("待取药"); // 修改病历状态

        int index=medicalRecordMapper.chargeMedicalRecore(medicalRecord);

        return index;
    }

    // 获取待退费列表
    @Override
    public PageInfo<MedicalRecord> showWaitRefund(Integer page, Integer limit, PatientInfo patientInfo) {

        MedicalRecord medicalRecord=new MedicalRecord();
        medicalRecord.setPatientInfo(patientInfo);
        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        List<MedicalRecord> medicalRecords=medicalRecordMapper.showWaitRefund(medicalRecord);
        PageInfo<MedicalRecord> medicalRecordsPageInfo =new PageInfo<>(medicalRecords);
        return medicalRecordsPageInfo;


    }


    // 获取西药退费列表
    @Override
    public List<Map<String, String>> showWaitRefundWBtch(String medicalRecordId) {

        List<Map<String, String>> waitRefundWPre=medicalRecordMapper.showWaitRefundWPre(medicalRecordId);

        return waitRefundWPre;
    }

    // 获取中药退费列表
    @Override
    public List<Map<String, String>> showWaitRefundCBtch(String medicalRecordId) {

        List<Map<String, String>> waitRefundCPre=medicalRecordMapper.showWaitRefundCPre(medicalRecordId);
        return waitRefundCPre;
    }

    // 获取治疗退费列表
    @Override
    public List<Map<String, String>> showWaitRefundProBtch(String medicalRecordId) {
        List<Map<String, String>> waitRefundPro=medicalRecordMapper.showWaitRefundPro(medicalRecordId);
        return waitRefundPro;
    }

    // 确认退款按钮
    @Override
    public boolean refundDetailClick(List<BigInteger> wpreBatch, List<BigInteger> cpreBatch, List<BigInteger> proBatch, BigInteger medicalRecordId) {

        List<WPreBatch> wPreBatches=new ArrayList<>();
        List<CPreBatch> cPreBatchList=new ArrayList<>();
        List<ProBatch> proBatchList=new ArrayList<>();
        MedicalRecord medicalRecord=new MedicalRecord();



        if(wpreBatch.size()!=0){
            // 整理列表，批量更新
            for (BigInteger id:wpreBatch){
                WPreBatch wPreBatch=new WPreBatch();
                wPreBatch.setId(id);
                wPreBatch.setNowReturnNumber(0); // 清空当前退药数据
                wPreBatches.add(wPreBatch);
            }

            // 批量更新
            wPreBatchMapper.updateRefundWPreBatch(wPreBatches);

        }

        if (cpreBatch.size()!=0){
            for (BigInteger id:cpreBatch){
                CPreBatch cPreBatch=new CPreBatch();
                cPreBatch.setId(id);
                cPreBatch.setNowReturnNumber(0); // 清空当前退药数据
                cPreBatchList.add(cPreBatch);
            }
            // 批量更新
            cPreBatchMapper.updateRefundCPreBatch(cPreBatchList);
        }

        if(proBatch.size()!=0){
            for (BigInteger id:proBatch){
                ProBatch proBatchP=new ProBatch();
                proBatchP.setId(id);
                proBatchP.setNowReturnNumber(0); // 清空当前退药数据
                proBatchList.add(proBatchP);
            }
            // 批量更新
            proBatchMapper.updateReturnProBath(proBatchList);
        }

        medicalRecord.setMedicalRecordId(medicalRecordId);
        medicalRecord.setIsReturn(0); // 退药标志清空

        int flag=medicalRecordMapper.updateRefundMedicalRecord(medicalRecord);

        if(flag==1){
            return true;
        }else {
            return false;
        }
    }

    // 获取历史处方单
    @Override
    public PageInfo<MedicalRecord> showHistoryList(Integer page, Integer limit, PatientInfo patientInfo) {

        MedicalRecord medicalRecord=new MedicalRecord();
        medicalRecord.setPatientInfo(patientInfo);
        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        List<MedicalRecord> medicalRecords=medicalRecordMapper.showHistoryList(medicalRecord);
        PageInfo<MedicalRecord> medicalRecordsPageInfo =new PageInfo<>(medicalRecords);
        return medicalRecordsPageInfo;

    }


}
