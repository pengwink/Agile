<!--
 * @Author: Pengwink
 * @Date: 2023-09-28 08:29:52
 * @LastEditTime: 2024-02-03 09:14:49
 * @LastEditors: Pengwink
 * @Description: 
 * @FilePath: \AgileBoot-Front-End\src\views\inventory\bom\bom-save.vue
 * 版权声明
-->
<script setup lang="ts">
import { reactive, ref } from "vue";
import { enableOptions } from "@/constants/constants";
import * as $bomApi from "@/api/inventory/bom";
import { addBomApi } from "@/api/inventory/bom";
import { message } from "@/utils/message";
import type { FormInstance } from "element-plus";
import { cloneDeep } from "@pureadmin/utils";

const formRef = ref<FormInstance>();
const pageData: any = reactive({
  dialogVisible: false,
  title: "新增Bom",
  formLoading: false,
  isUpdate: false,
  formData: {
    bomName: ""
  },
  formRules: {
    bomName: [{ required: true, message: "请输入Bom名称", trigger: "blur" }]
  }
});
const emits = defineEmits(["ok", "close"]);
const open = (data?: any, title?: string) => {
  pageData.formData = data || {
    bomName: ""
  };
  pageData.title = title || "新增Bom";
  pageData.isUpdate = !!pageData.formData.id;
  pageData.dialogVisible = true;
};
const handleClose = () => {
  pageData.dialogVisible = false;
  emits("close");
};
const handleConfirm = () => {
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
const _save = () => {
  pageData.formLoading = true;
  const _data = cloneDeep(pageData.formData);
  addBomApi(_data)
    .then((res: any) => {
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
    .update(pageData.formData.id, _data)
    .then((res: any) => {
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
defineOptions({ name: "BomSave" });
</script>

<template>
  <el-dialog
    v-model="pageData.dialogVisible"
    :title="pageData.title"
    destroy-on-close
    :width="400"
  >
    <div style="text-align: center">
      <el-form
        :model="pageData.formData"
        style="width: 70%"
        label-width="auto"
        :rules="pageData.formRules"
        ref="formRef"
        :loading="pageData.formLoading"
      >
        <el-form-item label="Bom名称" prop="bomName">
          <el-input
            v-model="pageData.formData.bomName"
            clearable
            placeholder="请输入Bom名称"
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
