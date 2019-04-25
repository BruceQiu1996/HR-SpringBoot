<template>
  <div>
    <el-container>
      <el-header style="text-align: left;padding-left: 0px;margin-top: 10px">
        <el-tag size="medium">请选择部门:</el-tag>
        <el-select size="mini" v-model="depId" placeholder="请选择" @change="depModify">
          <el-option
            v-for="item in deps"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-header>
      <el-table
            :data="emps"
            v-loading="tableLoading"
            border
            stripe
            size="mini"
            style="width: 1051px;margin-top:20px"
            >
            <el-table-column
              prop="name"
              width="200"
              align="center"
              label="姓名">
            </el-table-column>
            <el-table-column
              prop="conversionTime"
              width="200"
              align="center"
              label="转正时间">
              <template slot-scope="scope">{{ scope.row.conversionTime | formatDate}}</template>
            </el-table-column>
            <el-table-column
              prop="workID"
              align="center"
              width="450"
              label="工号">
            </el-table-column>
            <el-table-column
              prop="salaryNum"
              align="center"
              width="200"
              label="薪资(元)">
            </el-table-column>
          </el-table>
    </el-container>
  </div>
</template>
<script>
  export default{
    data(){
      return {
        depId: 1,
        deps: [],
        emps:[],
        tableLoading:false,
      }
    },
    methods: {
      loadDeps(){
        var _this = this;
        this.getRequest("/salary/table/deps").then(resp=> {
          if (resp && resp.status == 200) {
            _this.deps = resp.data;
          }
        })
      },
      loademps(){
        var _this=this;
        this.getRequest("/salary/table/salaryByDep/0").then(resp=> {
          if (resp && resp.status == 200) {
            _this.emps = resp.data;
          }
        })
      },
      depModify(id){
        var _this=this;
        this.getRequest("/salary/table/salaryByDep/"+id).then(resp=> {
          if (resp && resp.status == 200) {
            _this.emps = resp.data;

          }
        })
      }
    },
    mounted:function () {
      this.loadDeps();
      this.loademps();
    }
  }
</script>
