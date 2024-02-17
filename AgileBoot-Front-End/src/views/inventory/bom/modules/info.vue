<!--
 * @Author: Pengwink
 * @Date: 2023-08-12 17:56:29
 * @LastEditTime: 2024-02-12 10:47:54
 * @LastEditors: Pengwink
 * @Description: 
 * @FilePath: \AgileBoot-Front-End\src\views\inventory\bom\modules\info.vue
 * 版权声明
-->
<script setup lang="ts">
import { PureTableBar } from "@/components/RePureTableBar";
import FormSearch from "@/components/opts/form-search.vue";
import TableButtons from "@/components/opts/btns2.vue";
import { hasAuth } from "@/router/utils";
import BomEdit from "./bom-edit.vue";
import { onMounted, reactive, computed, ref, Transition } from "vue";
import * as $bomApi from "@/api/inventory/bom";
import { message } from "@/utils/message";
import { isString, isEmpty, clone, delay } from "@pureadmin/utils";
import EditPen from "@iconify-icons/ep/edit-pen";
import Check from "@iconify-icons/ep/check";
import { useRouter, useRoute } from "vue-router";
defineOptions({
  name: "Info"
});
// 编辑值（可多个）
const inputValMap = ref({});
// 是否正处于编辑状态（可多个）
const editStatus = ref({});
// 当前激活的单元格（唯一）
const activeIndex = ref(-1);
const router = useRouter();
const { query } = useRoute();
const select = ref("yes");
const bomEditRef = ref();
const tableData: any = reactive({
  permission: {
    query: ["bom:query:page"],
    info: ["bom:query:info"],
    save: ["bom:save"]
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
    productList: []
  },
  /*按钮 */
  btnOpts: {
    size: "small",
    leftBtns: [
      {
        key: "add",
        label: "新增",
        type: "primary",
        icon: "ep:plus",
        state: true,
        permission: ["org:save"]
      },
      {
        key: "update",
        label: "修改",
        type: "success",
        icon: "ep:edit",
        state: false,
        permission: ["org:update"]
      },
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
    columns: [
      {
        type: "selection",
        align: "left",
        reserveSelection: true,
        hide: () => (select.value === "no" ? true : false)
      },

      {
        label: "编码",
        prop: "encoding"
      },
      {
        label: "品名",
        prop: "productName"
      },
      {
        label: "型号",
        prop: "model"
      },
      {
        label: "规格",
        prop: "specification"
      },
      {
        label: "数量",
        prop: "number"
        // cellRenderer: ({ row, index }) => (
        //   <div
        //     class="flex-bc w-full h-[32px]"
        //     onMouseenter={() => (activeIndex.value = index)}
        //     onMouseleave={() => onMouseleave(index)}
        //   >
        //     <p v-show={!editing.value(index)}>{row.number}</p>
        //     <Transition enter-active-class="animate__animated animate__fadeInUp animate__faster">
        //       <el-input
        //         v-show={editing.value(index)}
        //         modelValue={comVal.value(index)}
        //         onInput={value => onChange(value, index)}
        //       />
        //     </Transition>
        //     <iconify-icon-offline
        //       v-show={editing.value(index)}
        //       icon={Check}
        //       class={iconClass.value(index)}
        //       onClick={() => onSure(index)}
        //     />
        //     <iconify-icon-offline
        //       v-show={activeIndex.value === index && !editing.value(index)}
        //       icon={EditPen}
        //       class={iconClass.value(index, true)}
        //       onClick={() => onEdit(row, index)}
        //     />
        //   </div>
        // )
      },
      {
        label: "操作",
        width: "120",
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
    tableData: []
  }
});
const dataList = ref(clone(tableData.tableParams.list, true));
// 更新输入框的数据
const _updateSearchFormData = (data: any) => {
  tableData.searchForm = data;
};
// 搜索框的数据
const _searchForm = (data: any) => {
  tableData.searchForm = data;
  _loadData();
};
// 重置搜索框
const _resetSearchForm = (data?) => {
  tableData.searchForm = data;
};
const _loadData = (query?: any) => {
  if (query == null) {
    return message.warning("为空");
  }
  tableData.tableParams.loading = true;
  console.log(query);
  $bomApi
    .queryInfo<any>(query)
    .then(res => {
      if (res.success) {
        console.log(res.data);
        tableData.tableParams.list = res.data;
        console.log(tableData.tableParams.list);
      } else {
        message.warning(res.message);
        tableData.tableParams.list = [];
      }
    })
    .finally(() => {
      tableData.tableParams.loading = false;
    });
};
const exportExcel = () => {
  console.log(query);
  $bomApi.exportExcel(query.id).then((result: Result<any>) => {
    if (result) {
      const res = result as unknown as
        | string
        | Blob
        | ArrayBuffer
        | ArrayBufferView;
      fileDownload(res, "bom列表" + ".xlsx");
    } else {
      message.error("无法找到对应的文件！！！");
    }
  });
};
/**
 * @TODO:
 * @description 打开编辑页面或者查看详情
 * @param row
 */
const deleteRow = (row: any) => {
  message.confirm("确认删除当前数据").then(() => {
    console.log(row);
    batchDel([row]);
  });
};
const batchDel = (ids: string[]) => {
  if (ids && ids.length > 0) {
    $bomApi.infoDelete(ids).then((res: any) => {
      if (res.success) {
        message.success("删除成功");
        _loadData(query.id);
      } else {
        message.warning(res.message);
      }
    });
  }
};
const onAddItem = () => {
  console.log(tableData.tableParams.list);
  tableData.tableParams.list.push({
    productName: "",
    encoding: "",
    model: "",
    specification: ""
  });
};
const comVal = computed(() => {
  return index => {
    return inputValMap.value[index]?.value;
  };
});

const editing = computed(() => {
  return index => {
    return editStatus.value[index]?.editing;
  };
});

const iconClass = computed(() => {
  return (index, other = false) => {
    return [
      "cursor-pointer",
      "ml-2",
      "transition",
      "delay-100",
      other
        ? ["hover:scale-110", "hover:text-red-500"]
        : editing.value(index) && ["scale-150", "text-red-500"]
    ];
  };
});

function onEdit({ productName }, index) {
  inputValMap.value[index] = Object.assign({}, inputValMap.value[index], {
    value: productName
  });
  // 处于编辑状态
  editStatus.value[index] = Object.assign({}, editStatus.value[index], {
    editing: true
  });
}

function onMouseleave(index) {
  inputValMap.value[index]?.value
    ? (activeIndex.value = index)
    : (activeIndex.value = -1);
}

function onChange(value, index) {
  inputValMap.value[index].value = value;
}

function onSure(index) {
  dataList.value[index].id = inputValMap.value[index].value;
  message.success("您编辑了第 ${index + 1} 行}");
  // 编辑状态关闭
  editStatus.value[index] = Object.assign({}, editStatus.value[index], {
    editing: false
  });
  delay().then(() => (inputValMap.value[index].value = null));
}
const handleBtnClick = (val: String) => {
  switch (val) {
    case "add":
      _handlerAdd();
      break;
    case "update":
      break;
    case "search":
      _updateSearchState();
      break;
    case "refresh":
      _loadData(query.id);
      break;
  }
};
const _updateSearchState = () => {
  tableData.searchState = !tableData.searchState;
};
const _handlerAdd = () => {
  console.log("add");
  bomEditRef.value!.open(query.id);
};
const handleEdit = (data: any) => {
  bomEditRef.value!.open(query.id, data, "更换产品");
};
onMounted(() => {
  console.log(query);
  _loadData(query.id);
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
        :show="tableData.searchState"
        :form-field="tableData.searchField"
        :data-source="tableData.dataSource"
        @search-form="_updateSearchFormData"
        @search="_searchForm"
        @reset="_resetSearchForm"
        :query-permission="tableData.permission.query"
      />
    </el-form>

    <PureTableBar title="bom信息" :columns="tableData.tableParams.columns">
      <template #buttons>
        <el-button
          class="button-spacing"
          type="primary"
          :loading="tableData.tableParams.loading"
          @click="router.push({ name: 'Template' })"
        >
          下载模板
        </el-button>
        <el-button
          type="primary"
          :loading="tableData.tableParams.loading"
          @click="importExcel"
        >
          导入
        </el-button>
        <el-button
          type="primary"
          :loading="tableData.tableParams.loading"
          @click="exportExcel()"
        >
          导出
        </el-button>
        <el-button
          class="button-spacing"
          type="primary"
          :loading="tableData.tableParams.loading"
          @click="handleBtnClick('add')"
        >
          新增
        </el-button>
        <!-- <el-upload
          class="upload-demo"
          :on-success="uploadSuccess"
          accept=".xlsx,.xls"
          :show-file-list="false"
          multiple
          :limit="1"
          :on-exceed="handleExceed"
          action=""
          @http-request="importExcel"
        >
          <el-button type="primary" class="button-spacing">导入</el-button>
        </el-upload> -->
        <!-- <el-button class="button-spacing" type="primary" @click="exportExcel">
          导出
        </el-button> -->
      </template>

      <template v-slot="{ size, dynamicColumns }">
        <pure-table
          row-key="id"
          :data="tableData.tableParams.list"
          :columns="dynamicColumns"
          maxHeight="600"
        >
          <template #operation="{ row }">
            <el-button
              link
              type="primary"
              size="small"
              @click.prevent="handleEdit(row)"
            >
              更换
            </el-button>
            <el-button
              link
              type="primary"
              size="small"
              @click.prevent="deleteRow(row.id)"
            >
              删除
            </el-button>
          </template>
        </pure-table>
        <!-- <el-button class="mt-4" style="width: 100%"> 添加 </el-button> -->
        <bom-edit ref="bomEditRef" @ok="_loadData(query.id)" />
      </template>
    </PureTableBar>
  </div>
</template>
<style scoped>
.button-spacing {
  margin-right: 10px; /* 可以根据需要调整间距 */
}
</style>
