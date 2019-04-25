package org.sang.service;

import org.sang.bean.EmpSimpleDemo;
import org.sang.bean.EmpTrain;
import org.sang.bean.Employee;
import org.sang.bean.EmplyeeEcs;
import org.sang.bean.Nation;
import org.sang.bean.PoliticsStatus;
import org.sang.bean.TrainDetails;
import org.sang.mapper.EmpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sang on 2018/1/12.
 */
@Service
@Transactional
public class EmpService {
    @Autowired
    EmpMapper empMapper;
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public List<Nation> getAllNations() {
        return empMapper.getAllNations();
    }

    public List<PoliticsStatus> getAllPolitics() {
        return empMapper.getAllPolitics();
    }

    public int addEmp(Employee employee) {
        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        Double contractTerm = (Double.parseDouble(yearFormat.format(endContract)) - Double.parseDouble(yearFormat.format(beginContract))) * 12 + Double.parseDouble(monthFormat.format(endContract)) - Double.parseDouble(monthFormat.format(beginContract));
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(contractTerm / 12)));
        return empMapper.addEmp(employee);
    }

    public Long getMaxWorkID() {
        Long maxWorkID = empMapper.getMaxWorkID();
        return maxWorkID == null ? 0 : maxWorkID;
    }

    public List<Employee> getEmployeeByPage(Integer page, Integer size, String keywords, Long politicId, Long nationId, Long posId, Long jobLevelId, String engageForm, Long departmentId, String beginDateScope) {
        int start = (page - 1) * size;
        Date startBeginDate = null;
        Date endBeginDate = null;
        if (beginDateScope != null && beginDateScope.contains(",")) {
            try {
                String[] split = beginDateScope.split(",");
                startBeginDate = birthdayFormat.parse(split[0]);
                endBeginDate = birthdayFormat.parse(split[1]);
            } catch (ParseException e) {
            }
        }
        return empMapper.getEmployeeByPage(start, size, keywords, politicId, nationId, posId, jobLevelId, engageForm, departmentId, startBeginDate, endBeginDate);
    }

    public Long getCountByKeywords(String keywords, Long politicId, Long nationId, Long posId, Long jobLevelId, String engageForm, Long departmentId, String beginDateScope) {
        Date startBeginDate = null;
        Date endBeginDate = null;
        if (beginDateScope != null && beginDateScope.contains(",")) {
            try {
                String[] split = beginDateScope.split(",");
                startBeginDate = birthdayFormat.parse(split[0]);
                endBeginDate = birthdayFormat.parse(split[1]);
            } catch (ParseException e) {
            }
        }
        return empMapper.getCountByKeywords(keywords, politicId, nationId, posId, jobLevelId, engageForm, departmentId, startBeginDate, endBeginDate);
    }

    public int updateEmp(Employee employee) {
        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        Double contractTerm = (Double.parseDouble(yearFormat.format(endContract)) - Double.parseDouble(yearFormat.format(beginContract))) * 12 + Double.parseDouble(monthFormat.format(endContract)) - Double.parseDouble(monthFormat.format(beginContract));
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(contractTerm / 12)));
        return empMapper.updateEmp(employee);
    }

    public boolean deleteEmpById(String ids) {
        String[] split = ids.split(",");
        return empMapper.deleteEmpById(split) == split.length;
    }

    public List<Employee> getAllEmployees() {
        return empMapper.getEmployeeByPage(null, null, "", null, null, null, null, null, null, null, null);
    }

    public int addEmps(List<Employee> emps) {
        return empMapper.addEmps(emps);
    }

    public List<Employee> getEmployeeByPageShort(Integer page, Integer size) {
        int start = (page - 1) * size;
        return empMapper.getEmployeeByPageShort(start,size);
    }
    public List<EmplyeeEcs> selAllEcs() {
    	try {
    		return empMapper.selAllEcs();
    	}catch (Exception e) {
    		
			// TODO: handle exception
    		System.out.println(e.toString());
    		return null;
		}
	}
    
    public List<EmpSimpleDemo> selAddEmpceInfo(){
    	return empMapper.selAllEmplyeeBaseInfo();
    }
    public int addEmpec(EmplyeeEcs emplyeeEcs) {
    	return empMapper.addEmpEc(emplyeeEcs);
    }
    public int deleteEmpEc(Integer id) {
    	return empMapper.deleEmpEcByID(id);
    }
    public List<EmpTrain> selAllTrainInfo(){
    	return empMapper.selAllTrainsInfo();
    }
    public List<TrainDetails> selDetainsByID(Integer id){
    	return empMapper.selTrainDetailByID(id);
    }
    @Transactional
    public boolean addNewTrains(EmpTrain Train) {
    	empMapper.InsertNewTrain(Train);
    	String[] principles=Train.getSteps().split(";");
    	if(principles.length==0||Train.getEmps().length==0) {
    		try {
				throw new Exception("data error");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}finally{
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
    	}else {
    		int insertID=empMapper.getLastID();
    		for(int i=0;i<principles.length;i++) {
    			empMapper.InsertPrinciple(insertID,i+1,principles[i]);
    		}
    		for(int i=0;i<Train.getEmps().length;i++) {
    			empMapper.InsertRelate(Train.getEmps()[i],insertID);
    		}
    	}
    	return true;
    }
}
