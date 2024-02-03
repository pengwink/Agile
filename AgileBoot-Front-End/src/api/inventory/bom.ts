/*
 * @Author: Pengwink
 * @Date: 2023-09-14 11:18:19
 * @LastEditTime: 2024-02-03 10:29:24
 * @LastEditors: Pengwink
 * @Description:
 * @FilePath: \AgileBoot-Front-End\src\api\inventory\bom.ts
 * 版权声明
 */
import { http } from "@/utils/http";
import {
  Result,
  deleteRequest,
  get,
  post,
  put,
  imExcel,
  exExcel
} from "../base";
export interface BomQuery extends BasePageQuery {
  bomName: string;
  creatorName: string;
}

type BomDTO = {
  bomId: string;
  bomName: string;
  createTime: Date;
  creatorName: string;
};

export function getBomListApi<T>(params?: BomQuery) {
  return http.request<ResponseData<PageDTO<BomDTO>>>("get", "/inventory/boms", {
    params
  });
}

export interface AddBomCommand {
  bomName: string;
}

export const addBomApi = (data: AddBomCommand) => {
  return http.request<ResponseData<void>>("post", "/inventory/boms", {
    data
  });
};
export const deleteBomApi = (data: Array<number>) => {
  return http.request<ResponseData<void>>("delete", `/inventory/boms/`, {
    params: {
      ids: data.toString()
    }
  });
};
