<template>
  <div>
    <el-container>
      <el-header style="padding: 0px;display:flex;justify-content:space-between;align-items: center">
        <div style="display: inline">
          <el-input
            placeholder="通过员工名搜索员工,记得回车哦..."
            clearable
           @change="keywordsChange"
            style="width: 300px;margin: 0px;padding: 0px;"
            size="mini"
            @keyup.enter.native="searchEmp"
            
            prefix-icon="el-icon-search"
            v-model="keywords">
          </el-input>
          <el-button type="primary" size="mini" style="margin-left: 5px" icon="el-icon-search" @click="searchEmpEc">搜索
          </el-button>
        </div>
      </el-header>
    <el-table
            :data="empeecs"
            v-loading="tableLoading"
            border
            stripe

            size="mini"
            style="width: 100%">
            <el-table-column
              prop="employee.name"
              align="left"
              fixed
              label="姓名"
              width="90">
            </el-table-column>
            <el-table-column
              prop="employee.id"
              width="85"
              align="center"
              label="工号">
            </el-table-column>
            <el-table-column
              prop="employee.email"
              width="180"
              align="left"
              label="电子邮件">
            </el-table-column>
            <el-table-column
              prop="employee.phone"
              width="100"
              label="电话号码">
            </el-table-column>
            <el-table-column
              align="center"
              width="150"
              label="记录时间">
              <template slot-scope="scope">{{ scope.row.recordtime | formatDate}}</template>
            </el-table-column>
            <el-table-column
              prop="reason"
              align="left"
              width="250"
              label="奖罚原因">
            </el-table-column>
            <el-table-column
              prop="count"
              align="center"
              width="100"
              label="奖罚积分">
            </el-table-column>
            <el-table-column
              prop="remark"
              align="center"
              width="450"
              label="备注">
            </el-table-column>
            <el-table-column
              align="center"
              width="100"
              label="类型">
              <template slot-scope="scope">{{scope.row.type|formatType}}</template>
            </el-table-column>
            <el-table-column
              fixed="right"
              align="center"
              label="操作"
              width="150">
              <template slot-scope="scope">
                <el-button type="danger" style="padding: 3px 4px 3px 4px;margin: 2px;" size="mini"
                           @click="deleteEmp(scope.row)">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
           <el-button type="primary" size="mini" icon="el-icon-plus"
                     @click="showAddEcs">
            添加奖惩事件
          </el-button>
          <!--添加界面-->
           <el-form :model="empec" :rules="rules" ref="addEmpecForm" style="margin: 0px;padding: 0px;">
            <div style="text-align: left">
              <el-dialog
                :title="dialogtitle"
                style="padding: 0px;"
                :close-on-click-modal="false"
                :visible.sync="dialogVisible"
                width="70%">
                <el-row>
                <el-row>
                  <el-col :span="6">
                    <div>
                      <el-form-item label="员工:" prop="employeeId">
                        <el-select v-model="empec.employeeId" style="width: 150px" size="mini" placeholder="请选择员工">
                          <el-option
                            v-for="item in emps"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                          </el-option>
                        </el-select>
                      </el-form-item>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div>
                      <el-form-item label="事件日期:" prop="recordtime">
                        <el-date-picker
                          v-model="empec.recordtime"
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
                      <el-form-item label="原因:" prop="reason">
                        <el-input v-model="empec.reason" size="mini" style="width: 300px" placeholder="事由"></el-input>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
                <el-row>
                <el-col :span="5">
                  <div>
                <el-form-item label="类型:" prop="type">
                  <el-radio-group v-model="empec.type">
                    <el-radio label="0">奖励</el-radio>
                    <el-radio style="margin-left: 15px" label="1">处罚</el-radio>
                  </el-radio-group>
                </el-form-item>
                </div>
               </el-col>
               </el-row>
                <el-col :span="6">
                    <div>
                      <el-form-item label="奖罚积分:" prop="count">
                        <el-input v-model="empec.count" size="mini" style="width: 120px" placeholder="积分" type="number"></el-input>
                      </el-form-item>
                    </div>
                  </el-col>
                <el-col :span="7">
                    <div>
                      <el-form-item label="备注:" prop="remark">
                        <el-input v-model="empec.remark" size="mini" style="width: 300px" placeholder="备注"></el-input>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
                <span slot="footer" class="dialog-footer">
                <el-button size="mini" @click="cancelEidt">取 消</el-button>
                <el-button size="mini" type="primary" @click="addEmpec('addEmpecForm')">确 定</el-button>
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
        allempec:[],
        empeecs:[],
        tempecs:[],
        tableLoading: false,
        keywords: '',
        dialogVisible:false,
        empec:{
          employeeId:'',
          recordtime:'',
          reason:'',
          count:'',
          type:'',
          remark:'',
        },
        dialogtitle:'添加奖惩',
        rules: {
            reason: [{required: true, message: '必填:原因', trigger: 'blur'}],
            recordtime: [{required: true, message: '必填:时间', trigger: 'blur'}],
            employeeId: [{required: true, message: '必填:员工', trigger: 'blur'}],
            count: [{required: true, message: '必填:积分数', trigger: 'blur'}],
            type: [{required: true, message: '必填:类型', trigger: 'blur'}],
        },
        emps:[]
      }
    },
     mounted: function () {
      this.loadEmpEcs();
    },
    filters:{
      formatType:function(value){
          if(value==1){
              return "惩罚事件"
          }else{
            return "奖励事件"
          }
      }
    },
    methods: {
       loadEmpEcs(){
        var _this = this;
        this.tableLoading = true;
        this.getRequest("/employee/basic/EmpEcs").then(resp=> {
          this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.empeecs = data;
            _this.allempec=data;
          }
        })
      },
      deleteEmp(row){
          this.$confirm('此操作将永久删除[' + row.employee.name + '], 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.doDelete(row.id);
        }).catch(() => {
        });
      },
      doDelete(id){
        this.tableLoading = true;
        var _this = this;
        this.deleteRequest("/employee/basic/DeleteEmp/" + id).then(resp=> {
          _this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.loadEmpEcs();
          }
        })
      },
      searchEmpEc(){
        this.empeecs=this.allempec;
        this.tempecs=[];
        var _this=this;
        this.empeecs.map(function(item,index){
          if(item.employee.name.indexOf(_this.keywords)>=0){
            _this.tempecs.push(item);
          }
        });
        this.empeecs=this.tempecs;
      },
      keywordsChange(val){
        if (val == '') {
          this.loadEmpEcs();
        }
      },
      showAddEcs(){
        this.dialogVisible = true;
        var _this = this;
        this.getRequest("/employee/basic/AddEmpEc").then(resp=> {
          if (resp && resp.status == 200) {
            _this.emps = resp.data;
          }
        })
      },
      cancelEidt(){
        this.dialogVisible=false;
        this.emptyEmpec();
      },
      emptyEmpec(){
        this.empec={
          employeeId:'',
          recordtime:'',
          reason:'',
          count:'',
          type:'',
          remark:'',
        }
      },
      addEmpec(formName){
        var _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
              //添加
              this.tableLoading = true;
              this.postRequest("/employee/basic/addempEc", this.empec).then(resp=> {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.dialogVisible = false;
                  _this.emptyEmpec();
                  _this.loadEmpEcs();
                }
              })
          } else {
            return false;
          }
        });
      },
    },
  }
</script>
