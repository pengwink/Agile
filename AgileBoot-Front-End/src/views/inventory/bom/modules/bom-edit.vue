<!--
 * @Author: Pengwink
 * @Date: 2023-09-26 10:32:51
 * @LastEditTime: 2023-10-17 15:32:53
 * @LastEditors: Pengwink
 * @Description: 
 * @FilePath: \pure-admin-thin-main\src\views\inventory\bom\modules\bom-edit.vue
 * 版权声明
-->
<script setup lang="ts">
import { onMounted } from "vue";
import { reactive, ref } from "vue";
import { message } from "@/utils/message";
import type { FormInstance } from "element-plus";
import { cloneDeep } from "@pureadmin/utils";
import * as $bomApi from "@/api/inventory/bom";
import * as $productApi from "@/api/inventory/product";
const formRef = ref<FormInstance>();
const pageData: any = reactive({
  queryData: [],
  dialogVisible: false,
  title: "新增产品",
  formLoading: false,
  isUpdate: false,
  isSave: false,
  formData: {
    id: "",
    encoding: "",
    productName: "",
    model: "",
    specification: "",
    number: "",
    parentId: ""
  },
  formRules: {}
});
const emits = defineEmits(["ok", "close"]);
const open = (id?: number, data?: any, title?: string) => {
  pageData.formData = data || {
    id: "",
    encoding: "",
    productName: "",
    model: "",
    specification: "",
    number: "",
    parentId: id
  };
  pageData.title = title || "新增产品";
  pageData.isUpdate = !!pageData.formData.id;
  pageData.dialogVisible = true;
};
const handleClose = () => {
  pageData.dialogVisible = false;
  emits("close");
};
const handleConfirm = () => {
  console.log(pageData.formData);
  formRef.value!.validate((isValid: boolean) => {
    if (isValid) {
      const { id } = pageData.formData;
      if (id) {
        _update();
      } else {
        _save();
      }
    } else {
      message("表单校验失败", { type: "warning" });
    }
  });
};
const querySearch = (queryString: string) => {
  $productApi.queryList(queryString).then((res: any) => {
    if (res.code == 200) {
      pageData.queryData = res.data;
      console.log(pageData.queryData);
    } else {
      message(res.message, { type: "warning" });
    }
  });
};
const FilterEntityName = (queryString, cb) => {
  $productApi.productList(queryString).then((res: any) => {
    pageData.queryData = res.data;
    console.log(pageData.queryData);
    const productNameList2 = pageData.queryData;
    const results = (productNameList2 || []).filter(createFilter(queryString));
    cb(results);
  });
};
const createFilter = queryString => {
  return res => {
    return res.productName;
  };
};
const handleSelect = val => {
  console.log(val);
  pageData.formData.productName = val.productName;
  pageData.formData.encoding = val.encoding;
  pageData.formData.model = val.model;
  pageData.formData.specification = val.specification;
};
const clearSearch = val => {
  val = {};
};
const _save = () => {
  pageData.formLoading = true;
  console.log(pageData.formData);
  const _data = cloneDeep(pageData.formData);
  const form = new FormData();
  form.append("encoding", _data.encoding);
  form.append("productName", _data.productName);
  form.append("model", _data.model);
  form.append("specification", _data.specification);
  form.append("number", _data.number);
  form.append("parentId", _data.parentId);
  $bomApi
    .infoSave(form)
    .then((res: any) => {
      console.log(res);
      if (res.success) {
        _confirm();
      } else {
        message(res.message, { type: "warning" });
      }
    })
    .finally(() => {
      pageData.formLoading = false;
    });
};
const _update = () => {
  pageData.formLoading = true;
  const _data = cloneDeep(pageData.formData);
  $bomApi
    .infoUpdate(pageData.formData.id, _data)
    .then((res: any) => {
      console.log(res);
      if (res.success) {
        _confirm();
      } else {
        message(res.message, { type: "warning" });
      }
    })
    .finally(() => {
      pageData.formLoading = false;
    });
};
const _confirm = () => {
  pageData.dialogVisible = false;
  emits("ok");
};
defineExpose({ open });
defineOptions({ name: "BomEdit" });
onMounted(() => {
  querySearch;
});
</script>

<template>
  <el-dialog
    v-model="pageData.dialogVisible"
    :title="pageData.title"
    destroy-on-close
    :width="600"
    class="el-dialog-header"
  >
    <div class="el-dialog-content">
      <el-form
        :model="pageData.formData"
        style="width: 80%"
        label-width="auto"
        :rules="pageData.formRules"
        ref="formRef"
        :loading="pageData.formLoading"
      >
        <el-form-item label="产品名称" prop="productName">
          <el-autocomplete
            v-model="pageData.formData.productName"
            value-key="productName"
            :fetch-suggestions="FilterEntityName"
            :clear-icon-click="clearSearch(pageData.formData.productName)"
            :trigger-on-focus="false"
            clearable
            class="inline-input w-50"
            placeholder="请输入产品名称"
            @select="handleSelect"
          />
        </el-form-item>
        <el-form-item label="产品编码" prop="encoding">
          <el-input
            v-model="pageData.formData.encoding"
            clearable
            placeholder="请输入产品编码"
          />
        </el-form-item>
        <el-form-item label="描述" prop="model">
          <el-input
            v-model="pageData.formData.model"
            clearable
            placeholder="请输入型号"
            :disabled="true"
          />
        </el-form-item>
        <el-form-item label="规格" prop="specification">
          <el-input
            v-model="pageData.formData.specification"
            clearable
            placeholder="请输入规格"
            :disabled="true"
          />
        </el-form-item>
        <el-form-item label="配套数量" prop="number">
          <el-input
            v-model="pageData.formData.number"
            clearable
            placeholder="请输入数量"
          />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确认</el-button>
    </template>
  </el-dialog>
</template>
<style scoped></style>
