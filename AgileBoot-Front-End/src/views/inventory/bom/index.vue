<!--
 * @Author: Pengwink
 * @Date: 2023-08-12 17:56:29
 * @LastEditTime: 2024-02-03 10:33:42
 * @LastEditors: Pengwink
 * @Description: 
 * @FilePath: \AgileBoot-Front-End\src\views\inventory\bom\index.vue
 * 版权声明
-->
<script setup lang="ts">
import { PureTableBar } from "@/components/RePureTableBar";
import FormSearch from "@/components/opts/form-search.vue";
import TableButtons from "@/components/opts/btns2.vue";
import { hasAuth } from "@/router/utils";
import { onBeforeMount, onMounted, reactive, ref, toRaw } from "vue";
import {
  useRoute,
  useRouter,
  type LocationQueryRaw,
  type RouteParamsRaw
} from "vue-router";
import BomSave from "./bom-save.vue";
import * as $bomApi from "@/api/inventory/bom";
import { getBomListApi, deleteBomApi } from "@/api/inventory/bom";
import { message } from "@/utils/message";
import { Result } from "@/api/base";
import type { LoadingConfig } from "@pureadmin/table";
import { isString } from "@pureadmin/utils";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";

defineOptions({
  name: "Bom"
});
const router = useRouter();
const select = ref("yes");
const bomSaveRef = ref();
const pageLoading = ref(true);
// 加载动画配置
const loadingConfig = reactive<LoadingConfig>({
  text: "正在加载第一页...",
  viewBox: "-10, -10, 50, 50",
  spinner: `
        <path class="path" d="
          M 30 15
          L 28 17
          M 25.61 25.61
          A 15 15, 0, 0, 1, 15 30
          A 15 15, 0, 1, 1, 27.99 7.5
          L 15 15
        " style="stroke-width: 4px; fill: rgba(0, 0, 0, 0)"/>
      `
  // svg: "",
  // background: rgba()
});
// 定义数据
const pageData: any = reactive({
  permission: {
    query: ["inventory:bom:page"],
    info: ["inventory:bom:info"],
    save: ["inventory:bom:add"],
    edit: ["inventory:bom:edit"],
    remove: ["inventory:bom:remove"]
  },
  searchState: true,
  searchField: [
    {
      type: "input",
      label: "bom表名称",
      prop: "bomName",
      placeholder: "请输入bom表名称"
      // TODO: 产品类型下拉框
    }
  ],
  dataSource: {
    bomList: []
  },
  /*按钮 */
  btnOpts: {
    size: "small",
    leftBtns: [
      {
        key: "import",
        label: "导入",
        type: "primary",
        icon: "ep:folder-add",
        state: true,
        permission: ["product:import"]
      },
      {
        key: "export",
        label: "导出",
        type: "success",
        icon: "ep:folder-remove",
        state: true,
        permission: ["product:export"]
      }
    ],
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
    // table column
    columns: [
      {
        type: "selection",
        align: "left",
        reserveSelection: true,
        hide: () => (select.value === "no" ? true : false)
      },
      {
        label: "序号",
        prop: "bomId",
        sortable: true
      },
      {
        label: "BOM表名称",
        prop: "bomName"
      },
      {
        label: "创建时间",
        prop: "createTime",
        columnKey: "date"
      },
      {
        label: "创建者",
        prop: "creatorName"
      },
      {
        label: "操作",
        fixed: "right",
        slot: "operation"
      }
    ],
    /**
     * table data
     */
    list: [],
    /*加载 */
    loading: false,
    /*数据 */
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
// 更新输入框的数据
const _updateSearchFormData = (data: any) => {
  pageData.searchForm = data;
};
// 搜索框的数据
const _searchForm = (data: any) => {
  pageData.searchForm = data;
  _loadData();
};
// 重置搜索框
const _resetSearchForm = (data?) => {
  pageData.searchForm = data;
};
// 返回查询的数据
const getQueryParams = () => {
  const sqp = {};
  const param = Object.assign(sqp, pageData.searchForm);
  param.pageNum = pageData.tableParams.pagination.currentPage;
  param.pageSize = pageData.tableParams.pagination.pageSize;
  return param;
};
const btnClickHandle = (key, _) => {
  switch (key) {
    case "import":
      importExcel(_);
      break;
    case "export":
      exportExcel();
      break;
    case "search":
      pageData.searchState = !pageData.searchState;
      break;
    case "refresh":
      _loadData();
      break;
    case "switch":
      switchTable();
      break;
  }
};
const switchTable = () => {
  pageData.tableParams.pagination.currentPage = 1;
  _loadData();
};
const _loadData = (page?: number) => {
  const query = getQueryParams();
  if (page) {
    query.current = page;
  }
  pageLoading.value = true;
  getBomListApi<any>(query)
    .then(res => {
      console.log(res);
      if (res.code == 0) {
        pageData.tableParams.list = res.data.rows;
        pageData.tableParams.pagination.total = Number(res.data.total);
      } else {
        message(res.msg, { type: "warning" });
        pageData.tableParams.list = [];
        pageData.tableParams.pagination.total = 0;
      }
    })
    .finally(() => {
      pageLoading.value = false;
    });
};
// $bomApi
//   .queryPage<any>(query)
//   .then(res => {
//     if (res.success) {
//       console.log(res.data.records);
//       pageData.tableParams.list = res.data.records;
//       pageData.tableParams.pagination.total = Number(res.data.total);
//     } else {
//       message(res.message);
//       pageData.tableParams.list = [];
//       pageData.tableParams.pagination.total = 0;
//     }
//   })
// };
const loadBomList = () => {
  // $bomApi.queryList({ isEnable: 1 }).then(res => {
  //   if (res.success) {
  //     console.log(res.data);
  //     pageData.dataSource.bomList = res.data;
  //   }
  // });
};
/**
 * @TODO:
 * @description 打开编辑页面或者查看详情
 * @param row
 */
const exportExcel = () => {
  // $bomApi.exportExcel("/excel/exportExcel").then((result: Result<any>) => {
  //   if (true) {
  //     const res = result.data;
  //     fileDownload(res, "产品列表" + ".xls");
  //   } else {
  //     message.error("无法找到对应的文件！！！");
  //   }
  // });
};
const uploadSuccess = (item: any) => {};
const handleExceed = (item: any) => {};
const importExcel = (item: any) => {
  const form = new FormData();
  form.append("file", item.file);
  // $bomApi
  //   .importExcel(form)
  //   .then(res => {
  //     console.log(res);
  //     let data = res.data;
  //     if (res.success) {
  //       message.success("导入成功!");
  //       _loadData; //导入成功刷新列表
  //       this.addFormVisible = false;
  //     } else {
  //       message.warning(res.message);
  //     }
  //   })
  //   .catch(err => {});
};
const handleChangePageSize = (val: any) => {
  pageData.tableParams.pagination.pageSize = val;
  _loadData();
};
const handleChangeCurrentPage = (val: any) => {
  pageData.tableParams.pagination.currentPage = val;
  _loadData();
};
// 页面跳转
const handleInfo = (parameter: LocationQueryRaw | RouteParamsRaw) => {
  console.log(parameter);
  if (parameter.bomInfoList == null || parameter.bomInfoList == "") {
    router.push({
      name: "BomInfo"
    });
  } else {
    Object.keys(parameter).forEach(param => {
      if (!isString(parameter[param])) {
        parameter[param] = parameter[param].toString();
      }
    });
    console.log(parameter.id);

    // 保存信息到标签页
    useMultiTagsStoreHook().handleTags("push", {
      path: `/inventory/bom/modules/bom-info`,
      name: "BomInfo",
      query: {
        id: parameter.id
      },
      meta: {
        // 如果使用的是非国际化精简版title可以像下面这么写
        title: `详情信息`,
        // 最大打开标签数
        dynamicLevel: 3
      }
    });

    // 路由跳转
    router.push({
      name: "BomInfo",
      query: {
        id: parameter.id
      }
    });
  }
};
const _handleAdd = () => {
  bomSaveRef.value!.open();
};
const _handleEdit = (data: any) => {
  bomSaveRef.value!.open(data, "修改Bom表");
};
async function handleDelete(row) {
  await deleteBomApi([row.bomId]).then(() => {
    message(`您删除了编号为${row.bomId}的这条岗位数据`, {
      type: "success"
    });
    _loadData();
  });
}
onBeforeMount(() => {
  loadBomList();
});
onMounted(() => {
  _loadData();
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
    <PureTableBar
      title="bom列表"
      :columns="pageData.tableParams.columns"
      @refresh="_searchForm"
    >
      <template #buttons>
        <el-button
          class="button-spacing"
          type="primary"
          v-show="hasAuth(pageData.permission.save)"
          :loading="pageData.tableParams.loading"
          @click="_handleAdd"
        >
          新增
        </el-button>
      </template>
      <template v-slot="{ size, dynamicColumns }">
        <pure-table
          border
          row-key="id"
          alignWhole="center"
          showOverflowTooltip
          table-layout="auto"
          :size="size"
          :loading="pageData.tableParams.loading"
          :loading-config="loadingConfig"
          :data="pageData.tableParams.list"
          :columns="dynamicColumns"
          :pagination="pageData.tableParams.pagination"
          @page-size-change="handleChangePageSize"
          @page-current-change="handleChangeCurrentPage"
          :header-cell-style="{
            background: 'var(--el-table-row-hover-bg-color)',
            color: 'var(--el-text-color-primary)'
          }"
        >
          <template #operation="{ row }">
            <el-link
              v-show="hasAuth(pageData.permission.info)"
              type="primary"
              @click="handleInfo(row)"
              >详细信息</el-link
            >
            <el-divider
              v-show="hasAuth(pageData.permission.edit)"
              direction="vertical"
            />
            <el-link
              v-show="hasAuth(pageData.permission.edit)"
              type="primary"
              @click="_handleEdit(row)"
              >编辑</el-link
            >
            <el-divider
              v-show="hasAuth(pageData.permission.remove)"
              direction="vertical"
            />
            <el-popconfirm
              :title="`是否确认删除编号为${row.bomName}的Bom的所有信息`"
              @confirm="handleDelete(row)"
            >
              <template #reference>
                <el-link
                  v-show="hasAuth(pageData.permission.remove)"
                  type="primary"
                  >删除</el-link
                >
              </template>
            </el-popconfirm>
          </template>
        </pure-table>
        <bom-save ref="bomSaveRef" @ok="_loadData()" />
      </template>
    </PureTableBar>
  </div>
</template>
<style scoped>
.button-spacing {
  margin-right: 10px; /* 可以根据需要调整间距 */
}
</style>
