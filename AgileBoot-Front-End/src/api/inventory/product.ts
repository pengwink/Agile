/*
 * @Author: Pengwink
 * @Date: 2023-09-14 11:18:19
 * @LastEditTime: 2024-01-31 15:15:05
 * @LastEditors: Pengwink
 * @Description:
 * @FilePath: \AgileBoot-Front-End\src\api\inventory\product.ts
 * 版权声明
 */
import { Result, get } from "../base";
import { http } from "@/utils/http";

export interface ProductQuery extends BasePageQuery {
  productName?: string;
  encoding?: string;
}
export interface AddProductCommand {
  encoding: string;
  productName: string;
  codingArea: string;
  type: string;
  model: string;
  specification: string;
  region: string;
  location: string;
  storageNumber: string;
  warningNumber: string;
  unit: string;
  remark: string;
}
export interface UpdateProductCommand extends AddProductCommand {
  productId: number;
}
export interface ProductPageResponse {
  productId: number;
  encoding: string;
  productName: string;
  codingArea: string;
  type: string;
  model: string;
  specification: string;
  region: string;
  location: string;
  storageNumber: string;
  warningNumber: string;
  unit: string;
  remark: string;
  createdTime: string;
}

export function queryProductPageApi<T>(query?: any): Promise<Result<T>> {
  return get("/inventory/product/list", query);
}

export function queryProductSelectApi<T>(): Promise<Result<T>> {
  return get("/inventory/product/select");
}
export function exportProductExcelApi(params: ProductQuery, fileName: string) {
  return http.download("/inventory/product/excel", fileName, {
    params
  });
}
export const addProductApi = (data: AddProductCommand) => {
  return http.request<ResponseData<void>>("post", "/inventory/product", {
    data
  });
};

export const updateProductApi = (data: UpdateProductCommand) => {
  return http.request<ResponseData<void>>("put", "/inventory/product", {
    data
  });
};
export const deleteProductApi = (data: Array<number>) => {
  return http.request<ResponseData<void>>("delete", "/inventory/product", {
    params: {
      // 需要将数组转换为字符串  否则Axios会将参数变成 noticeIds[0]:1  noticeIds[1]:2 这种格式，后端接收参数不成功
      ids: data.toString()
    }
  });
};
