<template>
<div>
    <el-container>
        <el-header style="padding: 0px;display:flex;justify-content:space-between;align-items: center;margin-top:20px">
        <div style="display: inline">
          <el-button type="primary" size="mini" style="margin-left: 5px" icon="el-icon-circle-plus-outline" @click="addTrain">添加培训
          </el-button>
        </div>
      </el-header>
        <el-table
            :data="trains"
            v-loading="tableLoading"
            border
            stripe
            size="mini"
            style="width: 100%;margin-top:20px"
            :row-class-name="tableRowClassName"
            >
            <el-table-column
              prop="id"
              align="center"
              fixed
              label="编号"
              width="90">
            </el-table-column>
            <el-table-column
              prop="trainContent"
              width="250"
              align="center"
              label="培训内容">
            </el-table-column>
            <el-table-column
              prop="trainDate"
              width="200"
              align="center"
              label="开始时间">
              <template slot-scope="scope">{{ scope.row.trainDate | formatDate}}</template>
            </el-table-column>
            <el-table-column
              prop="trainendDate"
              width="200"
              align="center"
              label="结束时间">
              <template slot-scope="scope">{{ scope.row.trainendDate | formatDate}}</template>
            </el-table-column>
            <el-table-column
              prop="remark"
              align="center"
              width="450"
              label="备注">
            </el-table-column>
             <el-table-column
              prop="state"
              align="center"
              width="100"
              label="状态">
              <template slot-scope="scope">{{ scope.row.state | formatState}}</template>
            </el-table-column>
             <el-table-column
              fixed="right"
              align="center"
              label="操作"
              width="250">
              <template slot-scope="scope">
                <el-button type="primary" style="padding: 3px 4px 3px 4px;margin: 2px;" size="mini"
                           @click="showDetails(scope.row)">查看详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-dialog
                :title="dialogtitle"
                style="padding: 0px;"
                :close-on-click-modal="false"
                :visible.sync="dialogVisible"
                width="400px">
                    <el-steps :space="200" :active="currentStep" finish-status="success" >
                        <el-step
                            v-for="item in trainsDetails"
                            :key="item.id"
                            :title="item.stepContent"
                            :value="item.id">
                          </el-step>
                    </el-steps>
                    <el-table
                         :data="currentEmps"
                         v-loading="tableLoading"
                        border
                        stripe
                        size="mini"
                        style="width: 342px"
                        >
                        <el-table-column
                        prop="depname"
                        align="center"
                        fixed
                        label="所属部门"
                        width="90">
                        </el-table-column>
                        <el-table-column
                        prop="name"
                        width="250"
                        align="center"
                        label="员工姓名">
                        </el-table-column>
                    </el-table>
            </el-dialog>
            <el-form :model="Train" style="margin: 0px;padding: 0px;" ref="addTrainForm" :rules="rules">
            <div style="text-align: left">
              <el-dialog
                :title="dialogtitle"
                style="padding: 0px;"
                :close-on-click-modal="false"
                :visible.sync="addDialogVisible"
                width="40%">
                <el-row>
                  <el-col :span="6">
                    <div>
                      <el-form-item label="培训课程名" prop="trainContent">
                        <el-input v-model="Train.trainContent" style="width: 150px" size="mini" placeholder="培训课程名">
                        </el-input>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="6">
                    <div>
                      <el-form-item label="开始日期:" prop="trainDate">
                        <el-date-picker
                          v-model="Train.trainDate"
                          size="mini"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          style="width: 150px"
                          type="date"
                          placeholder="记录时间">
                        </el-date-picker>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="6">
                    <div>
                      <el-form-item label="结束日期:" prop="trainendDate">
                        <el-date-picker
                          v-model="Train.trainendDate"
                          size="mini"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          style="width: 150px"
                          type="date"
                          placeholder="记录时间">
                        </el-date-picker>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
                  <el-row>
                  <el-col :span="7">
                    <div>
                      <el-form-item label="备注:" prop="remark">
                        <el-input v-model="Train.remark" size="mini" style="width: 400px" placeholder="备注"></el-input>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
                <hr style="border:1px solid #eeeeee"/>
                <el-row>
                  <el-col :span="7">
                    <div>
                      <el-form-item label="阶段(多个用分号隔开):" prop="steps">
                        <el-input v-model="Train.steps" size="mini" style="width: 400px" placeholder="培训阶段"></el-input>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="50">
                      <template>
                            <el-transfer v-model="values" :data="emps" :titles="titles"></el-transfer>
                        </template>
                  </el-col>
                </el-row>
                <span slot="footer" class="dialog-footer">
                <el-button size="mini" @click="cancelEidt">取 消</el-button>
                <el-button size="mini" type="primary" @click="submitTrain('addTrainForm')">确 定</el-button>
                </span>
              </el-dialog>
            </div>
           </el-form>
    </el-container>
</div>
</template>
<script>
  export default {
    data() {
      return {
        emptrains:[],
        trains:[],
        trainsDetails:[],
        tableLoading: false,
        dialogtitle:'',
        dialogVisible:false,
        addDialogVisible:false,
        currentStep:0,
        currentEmps:[],
        emps:[],
        values:[],
        Train:{
            trainContent:'',
            trainDate:'',
            trainendDate:'',
            remark:'',
            steps:'',
            emps:[],
        },
        titles:['全部员工','已选员工'],
        rules: {
            trainContent: [{required: true, message: '必填', trigger: 'blur'}],
            trainDate: [{required: true, message: '必填', trigger: 'blur'}],
            trainendDate: [{required: true, message: '必填', trigger: 'blur'}],
            steps: [{required: true, message: '必填', trigger: 'blur'}],
        },
      }
    },
     mounted: function () {
      this.loadDatas();
    },
    filters:{
        formatState(value){
            if(value==0){
                return "已完成";
            }else{
                return "第"+value+"阶段";
            }
        }
    },
    methods: {
       loadDatas(){
           var _this=this;
           this.getRequest("/employee/basic/EmpTrains").then(resp=> {
           if (resp && resp.status == 200) {
                _this.emptrains = resp.data;
                _this.emptrains.map(function(item,index){
                    var flag=0;
                    _this.trains.map(function(trainitem,index){
                        if(trainitem.id==item.id){
                            flag++;
                        }
                    })
                    if(flag==0){
                        _this.trains.push(item);
                    }
                })
           }
        })
       },
       tableRowClassName({row, rowIndex}){
            if (row.state == 0) {
                return 'warning-row';
            } else{
                return 'success-row';
            }
       },
       showDetails(row){
           this.emptyemps();
           var _this=this;
           this.getRequest("/employee/basic/transDetail/"+row.id).then(resp=> {
                if (resp && resp.status == 200) {
                    _this.trainsDetails=resp.data;
                }
           })
           _this.dialogtitle='查看'+row.trainContent;
           _this.dialogVisible=true;
           _this.currentStep=row.state-1;
           _this.emptrains.map(function(item,index){
               if(item.id==row.id){
                   _this.currentEmps.push(item);
               }
           })
           if(row.state==0){
               _this.currentStep=100;
           }
       },
       emptyemps(){
           this.currentEmps=[];
       },
       addTrain(){
           var _this=this;
           _this.emps=[],
           this.getRequest("/employee/basic/AddEmpEc").then(resp=> {
                if (resp && resp.status == 200) {
                    resp.data.map(function(item,index){
                        _this.emps.push({
                            key:item.id,
                            label:item.name,
                        });
                    })
                }
            })
           this.dialogtitle="添加培训";
           this.addDialogVisible=true;

       },
       submitTrain(formName){
            var _this = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
              //添加
                    if(_this.values.length>0){
                        this.tableLoading = true;
                        _this.Train.emps=_this.values;
                        this.postRequest("/employee/basic/addempTrain", _this.Train).then(resp=> {
                            _this.tableLoading = false;
                            if (resp && resp.status == 200) {
                                var data = resp.data;
                                _this.addDialogVisible = false;
                                this.Train={
                                    trainContent:'',
                                    trainDate:'',
                                    trainendDate:'',
                                    remark:'',
                                    steps:'',
                                }
                                _this.values=[];
                            }
                        })
                    }else{
                        this.$message({
                            showClose: true,
                            message: '至少选择一名同事',
                            type: 'error'
                        });
                        return;
                    }
                }
            })
       },
       cancelEidt(){
           this.addDialogVisible=false;
           this.Train={
                trainContent:'',
                trainDate:'',
                trainendDate:'',
                remark:'',
                steps:'',
            }
       }
    }
  }
</script>
<style>
  .el-table .warning-row {
    background: oldlace;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }
</style>

