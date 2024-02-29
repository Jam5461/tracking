<template>
  <div>
    <div style="display: flex">
      <a-button size="large" class="editable-add-btn" @click="commodityVisible = true">
        <a-icon type="plus"/>
        新增运单
      </a-button>
      <a-input-search
          placeholder="请输入运单号或名称"
          enter-button="搜索运单或名称"
          style="width: 400px;margin-left: 20px"
          size="large"
          @search="onSearch"
      />
      <a-button style="margin-left: 10px" size="large" type="danger" @click="loadTableData">
        重置
      </a-button>
    </div>
    <a-table :loading="loading" :columns="columns" :data-source="data" :sortDirections="['ascend', 'descend']" rowKey="id">
      <a slot="name" slot-scope="text">{{ text }}</a>
      <span slot="customTitle"><a-icon type="smile-o"/> 运单号</span>
      <span slot="customTitle"><a-icon type="smile-o"/> 订单号</span>
      <span slot="action" slot-scope="text, record, index">
        <a-button @click="handleUpdate(record)" type="link"><a-icon type="edit"/> Update</a-button>
      <a-divider type="vertical"/>
         <a-button @click="handleDelete(record,index)" type="link"><a-icon type="delete"/> Delete</a-button>
      </span>
    </a-table>

    <a-modal
        title="商品信息"
        :closable="false"
        :visible="commodityVisible"
    >
      <a-form-model ref="ruleForm" :model="commodity">
        <a-form-model-item ref="name" label="商品名称" prop="name">
          <a-input v-model="commodity.name"/>
        </a-form-model-item>
        <a-form-model-item label="订单号" prop="description">
          <a-input v-model="commodity.description" type="textarea"/>
        </a-form-model-item>
        <a-form-model-item label="运单号" prop="trackingNumber">
          <a-input v-model="commodity.trackingNumber"/>
        </a-form-model-item>
      </a-form-model>
      <template slot="footer">
        <a-button key="back" @click="commodityVisible = false">
          Return
        </a-button>
        <a-button key="submit" type="primary" :loading="modalLoading" @click="submitCommodity">
          Submit
        </a-button>
      </template>
    </a-modal>

  </div>
</template>

<script>
import {DeleteCommodityById, FindAllCommodity, SaveCommodity} from "@/api/commodity";
import {SearchCommodity} from "../../api/commodity";

const columns = [
  {
    title: '商品名称',
    dataIndex: 'name',
    key: 'name',
    className: 'blue-column'
  },
  {
    title: '运单号',
    dataIndex: 'tracking',
    key: 'tracking',
  },
  {
    title: '订单号',
    dataIndex: 'order',
    key: 'order',
  },
  {
    title: '库存数量',
    dataIndex: 'count',
    key: 'age'
  },
  {
    title: '入库时间',
    dataIndex: 'createAt',
    key: 'createAt',
    defaultSortOrder: 'descend', // 默认上到下为由大到小的顺序
    sorter: (a, b) => { return a.createAt> b.createAt? 1 : -1 },
    sortField: 'createAt'
  },
  {
    title: '更多操作',
    key: 'action',
    scopedSlots: {customRender: 'action'},
  }
];

const data = [];

export default {
  data() {
    return {
      commodity: {
        name: '',
        order: '',
        tracking: '',
        count: 0,
      },
      loading: false,
      modalLoading: false,
      commodityVisible: false,
      commodityLoading: false,
      data: [],
      columns,
    };
  },

  mounted() {
    this.loadTableData()
  },

  // methods: {
  //   onSearch(value) {
  //     if (value){
  //       this.loading = true
  //       SearchCommodity(value).then((res) => {
  //         console.log(res)
  //         if (res.data.length === 0) {
  //           this.$message.warn("未搜索到任何数据")
  //           setTimeout(() => {
  //             this.loading = false
  //             this.data = res.data
  //           }, 600)
  //         } else {
  //           setTimeout(() => {
  //             this.$message.success("搜索到" + res.data.length + "个商品")
  //             this.loading = false
  //             this.data = res.data
  //           }, 600)
  //         }
  //       })
  //     }else {
  //       this.$message.warn("请输入搜索内容")
  //     }
  //   },

  methods: {
    onSearch(value) {
      if (value){
        this.loading = true
        if (isNaN(value)) {
          // 如果value是非数字，那么我们假设它是一个商品名
          SearchCommodity(value).then(this.handleSearchResult)
        } else {
          // 如果value是数字，那么我们假设它是一个运单号
          SearchCommodityByTracking(value).then(this.handleSearchResult)
        }
      }else {
        this.$message.warn("请输入搜索内容")
      }
    },
    handleSearchResult(res) {
      console.log(res)
      if (res.data.length === 0) {
        this.$message.warn("未搜索到任何数据")
        setTimeout(() => {
          this.loading = false
          this.data = res.data
        }, 600)
      } else {
        setTimeout(() => {
          this.$message.success("搜索到" + res.data.length + "个商品")
          this.loading = false
          this.data = res.data
        }, 600)
      }
    }
},

    loadTableData() {
      this.loading = true
      FindAllCommodity().then((res) => {
        setTimeout(() => {
          this.loading = false
          this.data = res.data
        }, 600)
      })
    },
    submitCommodity() {
      this.modalLoading = true
      SaveCommodity(this.commodity).then((res) => {
        if (res.status) {
          setTimeout(() => {
            this.modalLoading = false
            this.commodityVisible = false
            this.$message.success('商品信息提交成功');
            this.loadTableData()
          }, 600)
        } else {
          setTimeout(() => {
            this.modalLoading = false
          }, 600)
        }
      })
    },

    handleDelete(r, index) {
      DeleteCommodityById(r.id).then((res) => {
        if (res.status) this.$message.success('商品信息删除成功');
        this.loadTableData()
      })
      console.log(index)
    },

    handleUpdate(r) {
      this.commodity = r
      this.commodityVisible = true
    },

  };
</script>

<style scoped>
.editable-add-btn {
  margin-bottom: 15px;
}

.blue-column {
  background-color: lightblue !important;
}
</style>