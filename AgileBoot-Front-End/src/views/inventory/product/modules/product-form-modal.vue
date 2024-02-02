<!--
 * @Author: Pengwink
 * @Date: 2024-01-27 14:24:56
 * @LastEditTime: 2024-01-27 22:19:55
 * @LastEditors: Pengwink
 * @Description: 
 * @FilePath: \AgileBoot-Front-End\src\views\inventory\product\modules\product-form-modal.vue
 * 版权声明
-->
<script setup lang="ts">
import VDialog from "@/components/VDialog/VDialog.vue";
import { computed, reactive, ref } from "vue";
import {
  AddProductCommand,
  ProductPageResponse,
  UpdateProductCommand,
  addProductApi,
  updateProductApi
} from "@/api/inventory/product";
// import { useUserStoreHook } from "@/store/modules/user";
import { ElMessage, FormInstance, FormRules } from "element-plus";

interface Props {
  type: "add" | "update";
  modelValue: boolean;
  row?: ProductPageResponse;
}

const props = defineProps<Props>();
const emits = defineEmits<{
  (e: "update:modelValue", v: boolean): void;
  (e: "success"): void;
}>();

const visible = computed({
  get: () => props.modelValue,
  set(v) {
    emits("update:modelValue", v);
  }
});

const formData = reactive<AddProductCommand | UpdateProductCommand>({
  productId: 0,
  encoding: "",
  productName: "",
  codingArea: "",
  type: "",
  model: "",
  specification: "",
  region: "",
  location: "",
  storageNumber: "",
  warningNumber: "",
  unit: "",
  remark: ""
});

// const statusList = useUserStoreHook().dictionaryMap["common.status"];

const rules: FormRules = {
  productName: [
    {
      required: true,
      message: "产品名称不能为空"
    }
  ],
  encoding: [
    {
      required: true,
      message: "产品编码不能为空"
    }
  ]
};
const formRef = ref<FormInstance>();
function handleOpened() {
  if (props.row) {
    Object.assign(formData, props.row);
  } else {
    formRef.value?.resetFields();
  }
}

const loading = ref(false);
async function handleConfirm() {
  try {
    loading.value = true;
    if (props.type === "add") {
      await addProductApi(formData);
    } else if (props.type === "update") {
      await updateProductApi(formData as UpdateProductCommand);
    }
    ElMessage.info("提交成功");
    visible.value = false;
    emits("success");
  } catch (e) {
    console.error(e);
    ElMessage.error((e as Error)?.message || "提交失败");
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <v-dialog
    show-full-screen
    :fixed-body-height="false"
    use-body-scrolling
    :title="type === 'add' ? '新增产品' : '更新产品'"
    v-model="visible"
    :loading="loading"
    @confirm="handleConfirm"
    @cancel="visible = false"
    @opened="handleOpened"
  >
    <el-form :model="formData" label-width="120px" :rules="rules" ref="formRef">
      <el-form-item prop="productName" label="产品名称" required inline-message>
        <el-input v-model="formData.productName" />
      </el-form-item>
      <el-form-item prop="encoding" label="产品编码" required>
        <el-input v-model="formData.encoding" />
      </el-form-item>
      <el-form-item prop="codingArea" label="编码区域">
        <el-input v-model="formData.codingArea" />
      </el-form-item>
      <el-form-item prop="model" label="型号">
        <el-input v-model="formData.model" />
      </el-form-item>
      <el-form-item prop="specification" label="规格">
        <el-input v-model="formData.specification" />
      </el-form-item>
      <el-form-item prop="location" label="货位">
        <el-input v-model="formData.location" />
      </el-form-item>
      <el-form-item prop="storageNumber" label="数量">
        <el-input v-model="formData.storageNumber" />
      </el-form-item>
      <el-form-item prop="warningNumber" label="预警数量">
        <el-input v-model="formData.warningNumber" />
      </el-form-item>
      <el-form-item prop="unit" label="单位">
        <el-input v-model="formData.unit" />
      </el-form-item>
      <el-form-item prop="remark" label="备注" style="margin-bottom: 0">
        <el-input type="textarea" v-model="formData.remark" />
      </el-form-item>
    </el-form>
  </v-dialog>
</template>
