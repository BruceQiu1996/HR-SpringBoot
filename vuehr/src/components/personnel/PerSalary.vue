<template>
    <div>
        <el-container>
            <div style="margin:0 auto;margin-top:200px">
                <el-button type="success" style="width:300px;height:300px;font-size:30px;font-weight:bold" @click="depSalary">部门调薪</el-button>
                <el-button type="warning" style="width:300px;height:300px;font-size:30px;margin-left:100px;font-weight:bold" @click="empSalary">员工调薪</el-button>
            </div>
        </el-container>
        <el-form  style="margin: 0px;padding: 0px;" :model="depSalaryModel" ref="updepSalary" :rules="rules">
            <div style="text-align: left">
              <el-dialog
                :title="dialogtitle"
                style="padding: 0px;"
                :close-on-click-modal="false"
                :visible.sync="depDialogVisible"
                width="40%">
                <el-row>
                  <el-col :span="10">
                    <div>
                      <el-form-item label="部门:" prop='depid'>
                        <el-select style="width: 150px" size="mini" placeholder="请选择部门" @change="depModify" v-model="depSalaryModel.depid">
                          <el-option
                            v-for="item in deps"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                          </el-option>
                        </el-select>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="50">
                      <template>
                            <el-transfer v-model="depSalaryModel.values" :data="depemps" :titles="titles"></el-transfer>
                        </template>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="50">
                      <el-form-item label="涨幅点数:" prop="uppoint">
                      <template>
                        <el-input-number v-model="depSalaryModel.uppoint" :precision="2" :step="0.1" :max="100"></el-input-number>
                      </template>
                      </el-form-item>
                  </el-col>
                </el-row>
                <span slot="footer" class="dialog-footer">
                <el-button size="mini" @click="cancelEidt">取 消</el-button>
                <el-button size="mini" type="primary" @click="submit('updepSalary')">确 定</el-button>
                </span>
              </el-dialog>
            </div>
        </el-form>
        <el-form  style="margin: 0px;padding: 0px;" :model="empSalaryModel" ref="upEmpSalary" :rules="empRules">
            <div style="text-align: left">
              <el-dialog
                :title="dialogtitle"
                style="padding: 0px;"
                :close-on-click-modal="false"
                :visible.sync="empDialogVisible"
                width="40%">
                <el-row>
                  <el-col :span="50">
                      <template>
                            <el-transfer v-model="empSalaryModel.values" :data="emps" :titles="emptitles"></el-transfer>
                        </template>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="50">
                      <el-form-item label="涨幅点数:" prop="uppoint">
                      <template>
                        <el-input-number v-model="empSalaryModel.uppoint" :precision="2" :step="0.1" :max="100"></el-input-number>
                      </template>
                      </el-form-item>
                  </el-col>
                </el-row>
                <span slot="footer" class="dialog-footer">
                <el-button size="mini" @click="cancelEidt">取 消</el-button>
                <el-button size="mini" type="primary" @click="empsubmit('upEmpSalary')">确 定</el-button>
                </span>
              </el-dialog>
            </div>
        </el-form>
    </div>
</template>
<<script>
export default {
    data(){
        return{
            deps:[],
            dialogtitle:'',
            depDialogVisible:false,
            empDialogVisible:false,
            depemps:[],
            titles:['调薪员工','非调薪员工'],
            emptitles:['全部人员','调薪人员'],
            emps:[],
            depSalaryModel:{
                depid:'',
                uppoint:'',
                values:[],
            },
            tableLoading:'',
            rules:{
                emps:[{required: true, message: '必填:原因', trigger: 'blur'}],
                depid:[{required: true, message: '必填:原因', trigger: 'blur'}],
                uppoint:[{required: true, message: '必填:原因', trigger: 'blur'}],
            },
            empSalaryModel:{
                uppoint:'',
                values:[],
            },
            empRules:{
                uppoint:[{required: true, message: '必填:原因', trigger: 'blur'}],
            },
        }
    },
    mounted:function() {
        this.initData();
    },
    methods: {
        initData(){
            var _this = this;
            this.emps=[],
            this.getRequest("/salary/table/deps").then(resp=> {
            if (resp && resp.status == 200) {
                _this.deps = resp.data;
            }
            })
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
        },
        depSalary(){
            this.depDialogVisible=true;
        },
        empSalary(){
            this.empDialogVisible=true;
        },
        depModify(id){
            var _this=this;
            this.depemps=[];
            this.depSalaryModel.values=[];
            this.getRequest("/salary/table/salaryByDep/"+id).then(resp=> {
                if (resp && resp.status == 200) {
                    if(resp.data.length>0){
                        resp.data.map(function(item,index){
                            _this.depemps.push({
                                key: item.id,
                                label: item.name,
                            })
                        })
                    }else{
                        this.$message({
                            showClose: true,
                            message: '部门没有员工',
                            type: 'error'
                        });
                        return;
                    }
                }
            })
         },
         cancelEidt(){
             this.depDialogVisible=false;
             this.empDialogVisible=false;
             this.depemps=[],
             this.depSalaryModel={
                depid:'',
                uppoint:'',
                values:[],
             },
            this.empSalaryModel={
                uppoint:'',
                values:[],
            }
         },
         submit(formName){
             var _this = this;
             this.$refs[formName].validate((valid) => {
                if (valid) {
                    if(_this.depSalaryModel.uppoint=='0.00'){
                        this.$message({
                            showClose: true,
                            message: '增长数不可为0',
                            type: 'error'
                        });
                        return;
                    }
                    if(_this.depemps.length==0){
                        this.$message({
                            showClose: true,
                            message: '部门无人',
                            type: 'error'
                        });
                        return;
                    }
                    this.postRequest("/salary/sob/salary/up", _this.depSalaryModel).then(resp=> {
                        _this.tableLoading = false;
                        if (resp && resp.status == 200) {
                            var data = resp.data;
                            _this.depDialogVisible=false;
                            _this.cancelEidt();
                        }
                    })
                }else{
                    return;
                }
            })
         },
         empsubmit(formName){
             var _this = this;
             this.$refs[formName].validate((valid) => {
                if (valid) {
                    if(_this.empSalaryModel.uppoint=='0.00'){
                        this.$message({
                            showClose: true,
                            message: '增长数不可为0',
                            type: 'error'
                        });
                        return;
                    }
                    if(_this.empSalaryModel.values.length==0){
                        this.$message({
                            showClose: true,
                            message: '请选择员工',
                            type: 'error'
                        });
                        return;
                    }
                    this.postRequest("/salary/sob/salary/empup", _this.empSalaryModel).then(resp=> {
                        _this.tableLoading = false;
                        if (resp && resp.status == 200) {
                            var data = resp.data;
                            _this.depDialogVisible=false;
                            _this.cancelEidt();
                        }
                    })
                }
             });
         }
    },
}
</script>
