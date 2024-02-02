<!--
 * @Author: Pengwink
 * @Date: 2023-08-12 17:56:29
 * @LastEditTime: 2024-02-01 17:23:21
 * @LastEditors: Pengwink
 * @Description: 
 * @FilePath: \AgileBoot-Front-End\src\views\inventory\product\index.vue
 * 版权声明
-->
<script setup lang="ts">
import { PureTableBar } from "@/components/RePureTableBar";
import FormSearch from "@/components/opts/form-search.vue";
import { PureTable } from "@pureadmin/table";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import EditPen from "@iconify-icons/ep/edit-pen";
import Delete from "@iconify-icons/ep/delete";
import View from "@iconify-icons/ep/view";
import { message } from "@/utils/message";
import { hasAuth } from "@/router/utils";
import { addDialog } from "@/components/ReDialog";
import uploadForm from "./modules/uploadForm.vue";
import ProductFormModal from "@/views/inventory/product/modules/product-form-modal.vue";
import { onMounted, reactive, ref, h } from "vue";
import { update } from "../../../api/inventory/bom";
import {
  ProductPageResponse,
  queryProductPageApi,
  queryProductSelectApi,
  exportProductExcelApi,
  deleteProductApi
} from "@/api/inventory/product";
defineOptions({
  name: "product"
});
const productEditRef = ref();
const opType = ref<"add" | "update">("add");
const modalVisible = ref(false);
const pageLoading = ref(true);
const opRow = ref<ProductPageResponse>();
const pageData: any = reactive({
  content: null,
  fileURL: null,
  contentDialog: false,
  permission: {
    add: ["inventory:product:add"],
    edit: ["inventory:product:edit"],
    info: ["inventory:product:info"],
    remove: ["inventory:product:remove"],
    export: ["inventory:product:export"],
    import: ["inventory:product:import"],
    download: ["inventory:product:download"],
    query: ["inventory:product:query"]
  },
  searchState: true,
  searchField: [
    {
      type: "select",
      label: "产品名称",
      prop: "productName",
      placeholder: "请选择",
      dataSourceKey: "productOptions",
      options: {
        keys: {
          label: "productName",
          value: "productName",
          prop: "productName"
        },
        filterable: true
      }
    },
    {
      type: "select",
      label: "产品编码",
      prop: "encoding",
      placeholder: "模糊查询产品编码",
      dataSourceKey: "encodingOptions",
      options: {
        keys: {
          label: "encoding",
          value: "encoding",
          prop: "encoding"
        },
        filterable: true
      }
    }
  ],
  dataSource: {
    encodingOptions: {},
    productOptions: {},
    productList: []
  },
  /*按钮 */
  btnOpts: {
    size: "small",
    rightBtns: [
      {
        key: "search",
        label: "查询",
        icon: "ep:search",
        state: true,
        options: {
          circle: true
        }
      },
      {
        key: "refresh",
        label: "刷新",
        icon: "ep:refresh",
        state: true,
        options: {
          circle: true
        }
      },
      {
        key: "switch",
        label: "切换",
        icon: "ep:switch",
        state: true,
        options: {
          circle: true
        }
      }
    ]
  },
  // 搜索Data
  searchForm: {},
  // 表格
  tableParams: {
    /**
     * table column
     */
    columns: [
      {
        label: "产品编码",
        prop: "encoding"
      },
      {
        label: "产品名称",
        prop: "productName"
      },
      {
        label: "编码区域",
        prop: "codingArea"
      },
      {
        label: "产品型号",
        prop: "model"
      },
      {
        label: "规格",
        prop: "specification"
      },
      {
        label: "区域",
        prop: "region"
      },
      {
        label: "库位",
        prop: "location"
      },
      {
        label: "现有库存",
        prop: "endingNumber"
      },
      {
        label: "预警库存",
        prop: "warningNumber"
      },
      {
        label: "操作",
        fixed: "right",
        slot: "operation"
      }
    ],
    list: [],
    tableData: [],
    currentData: {},
    pagination: {
      pageSize: 10,
      defaultPageSize: 10,
      currentPage: 1,
      background: true,
      total: 0
    }
  }
});
/**
 * @param data 搜索框的数据
 * @description 更新输入框的数据
 */
const _updateSearchFormData = (data: any) => {
  pageData.searchForm = data;
};
/**
 * @param data 搜索框的数据
 * @description 获取收缩框中的数据并进行查询
 */
const _searchForm = (data: any) => {
  pageData.searchForm = data;
  _loadData();
};
/**
 * @param data
 * @description 重置搜索框
 */
const _resetSearchForm = (data?) => {
  pageData.searchForm = data;
  _loadData();
};
/**
 * @returns 返回查询的数据
 */
const getQueryParams = () => {
  const sqp = {};
  const param = Object.assign(sqp, pageData.searchForm);
  param.pageNum = pageData.tableParams.pagination.currentPage;
  param.pageSize = pageData.tableParams.pagination.pageSize;
  return param;
};
const _loadData = (page?: number) => {
  const query = getQueryParams();
  if (page) {
    query.pageNum = page;
  }
  pageLoading.value = true;
  queryProductPageApi<any>(query)
    .then(res => {
      console.log(res);
      if (res.code == 0) {
        pageData.tableParams.list = res.data.rows;
        pageData.tableParams.pagination.total = Number(res.data.total);
      } else {
        message(res.message, { type: "warning" });
        pageData.tableParams.list = [];
        pageData.tableParams.pagination.total = 0;
      }
    })
    .finally(() => {
      pageLoading.value = false;
    });
};
const _select = () => {
  queryProductSelectApi().then(res => {
    if (res.code == 0) {
      pageData.dataSource.encodingOptions = res.data.encoding;
      pageData.dataSource.productOptions = res.data.product;
    }
  });
};
/**
 * @TODO:
 * @description 打开编辑页面或者查看详情
 * @param row
 */
const handleAdd = (row?: any) => {
  const data: any = {};
  if (row) {
    data.parentId = row.id;
  }
  productEditRef.value!.open(data, pageData.dataSource, "add");
};
const exportExcel = () => {
  const query = getQueryParams();
  exportProductExcelApi(query, "产品数据.xlsx");
};

const handleChangePageSize = (val: any) => {
  pageData.tableParams.pagination.pageSize = val;
  _loadData();
};
const handleChangeCurrentPage = (val: any) => {
  pageData.tableParams.pagination.currentPage = val;
  _loadData();
};
function cellStyle({ column: { property }, row }) {
  if (property === "warningNumber") {
    return row.endingNumber - row.warningNumber >= 0
      ? { background: "#FFFFFF" }
      : { background: "#FF0000" };
  }
}

function openDialog(type: "add" | "update", row?: ProductPageResponse) {
  opType.value = type;
  opRow.value = row;
  modalVisible.value = true;
}
async function handleDelete(row) {
  await deleteProductApi([row.productId]).then(() => {
    message(`您删除了编号为${row.productId}的这条岗位数据`, {
      type: "success"
    });
    _loadData();
  });
}
async function openUploadDialog() {
  const uploadFormRef = ref();
  addDialog({
    title: `导入产品`,
    props: {},
    width: "30%",
    closeOnClickModal: false,
    contentRenderer: () => h(uploadForm, { ref: uploadFormRef }),
    beforeSure: done => {
      console.log("上传文件");
      uploadFormRef.value.getFormRef().submit();
      done();
      _loadData();
    }
  });
}
onMounted(async () => {
  _loadData();
  _select();
});
</script>

<template>
  <div>
    <el-form
      ref="formRef"
      :inline="true"
      class="search-form bg-bg_color w-[99/100] pl-8 pt-[12px]"
    >
      <form-search
        :show="pageData.searchState"
        :form-field="pageData.searchField"
        :data-source="pageData.dataSource"
        @search-form="_updateSearchFormData"
        @search="_searchForm"
        @reset="_resetSearchForm"
        :query-permission="pageData.permission.query"
      />
    </el-form>
    <!-- 详情预览 -->
    <PureTableBar
      title="产品列表"
      :columns="pageData.tableParams.columns"
      @refresh="_searchForm"
    >
      <template #buttons>
        <div style="display: flex; flex-wrap: wrap">
          <el-button
            v-show="hasAuth(pageData.permission.add)"
            type="primary"
            :icon="useRenderIcon('ep:plus')"
            @click="openDialog('add')"
            >新增</el-button
          >
          <el-button
            v-show="hasAuth(pageData.permission.import)"
            type="primary"
            :icon="useRenderIcon('ep:folder-add')"
            @click="openUploadDialog"
            >导入</el-button
          >
          <el-button
            v-show="hasAuth(pageData.permission.export)"
            type="success"
            :icon="useRenderIcon('ep:folder-remove')"
            @click="exportExcel"
          >
            导出</el-button
          >
        </div>
      </template>
      <template v-slot="{ size, dynamicColumns }">
        <pure-table
          :data="pageData.tableParams.list"
          :columns="dynamicColumns"
          :size="size"
          row-key="id"
          border
          stripe
          :cell-style="cellStyle"
          :header-row-class-name="'table-header'"
          :loading="pageLoading"
          :pagination="pageData.tableParams.pagination"
          @page-current-change="handleChangeCurrentPage"
          @page-size-change="handleChangePageSize"
          :header-cell-style="{
            background: 'var(--el-table-row-hover-bg-color)',
            color: 'var(--el-text-color-primary)'
          }"
        >
          <template #enableScope="scope">
            <el-tag v-if="scope.row.enable">启用</el-tag>
            <el-tag v-else type="info">禁用</el-tag>
          </template>
          <template #operation="{ row }">
            <!-- TODO 查看库存的流水未完成 -->
            <el-button
              class="reset-margin"
              link
              type="primary"
              :size="size"
              :icon="useRenderIcon(View)"
            >
              详情
            </el-button>
            <el-button
              class="reset-margin"
              link
              type="primary"
              :size="size"
              :icon="useRenderIcon(EditPen)"
              @click="openDialog('update', row)"
            >
              编辑
            </el-button>
            <el-popconfirm
              :title="`是否确认删除编号为${row.encoding}的这个产品`"
              @confirm="handleDelete(row)"
            >
              <template #reference>
                <el-button
                  class="reset-margin"
                  link
                  type="danger"
                  :size="size"
                  :icon="useRenderIcon(Delete)"
                >
                  删除
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </pure-table>
      </template>
    </PureTableBar>
    <product-form-modal
      v-model="modalVisible"
      :type="opType"
      :row="opRow"
      @success="_loadData()"
    />
  </div>
</template>

<style scoped lang="scss">
#content {
  max-height: calc(
    100vh - 300px
  ); /* 调整 200px 到对话框头部和底部外边距的总和 */
  overflow-y: auto; /* 当内容超出设定的最大高度时，显示滚动条 */
}
</style>
