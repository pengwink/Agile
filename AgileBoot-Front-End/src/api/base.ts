/*
 * @Author: Pengwink
 * @Date: 2023-09-05 16:28:34
 * @LastEditTime: 2023-12-26 14:41:51
 * @LastEditors: Pengwink
 * @Description:
 * @FilePath: \pure-admin-thin-main\src\api\base.ts
 * 版权声明
 */
import { http } from "@/utils/http";
export interface Result<T> {
  success: boolean;
  code: number;
  message: string;
  data?: any;
  timestamp: number;
}

export interface BaseQuery {
  size?: number;
  current?: number;
}
/**
 * post
 */
export function post<T, P>(url: string, data: T): Promise<Result<P>> {
  return http.post<T, Result<P>>(url, { data: data });
}
/**
 * get
 */
export function get<T, P>(url: string, params?: T): Promise<Result<P>> {
  return http.get<T, Result<P>>(url, { params: params });
}
/**
 * put
 * @param url path
 * @param params query
 * @param data body
 * @returns .
 */
export function put<T>(
  url: string,
  params?: any,
  data?: T
): Promise<Result<T>> {
  return http.request("put", url, { params: params, data: data });
}
/**
 * 删除
 * @param url .
 * @param params .
 * @param data .
 * @returns .
 */
export function deleteRequest<T>(
  url: string,
  params?: any,
  data?: any
): Promise<Result<T>> {
  return http.request("delete", url, { params: params, data: data });
}

export function exExcel<T, P>(url: string, data?: T): Promise<Result<P>> {
  return http.request<any>(
    "get",
    url,
    { data },
    // 自定义的axios配置在下面对象填写即可
    {
      responseType: "blob",
      headers: {
        "Access-Control-Allow-origin": "*",
        "Content-Type": "application/json; charset=UTF-8"
      }
    }
  );
  // return axios({
  //   method: "get",
  //   url: "http://localhost:8080/inventory/product/export_excel",
  //   responseType: "blob",
  //   data,
  //   headers: {
  //     "Access-Control-Allow-origin": "*",
  //     "Content-Type": "application/json; charset=UTF-8",
  //     "X-Access-Token":
  //       "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MDM1Njk2MTYsInVzZXJuYW1lIjoiY3hrIn0.L7lNfbJEwIdZPHSQ3LvSHBs9-ip_lWEYR-nquftLEAA"
  //   }
  // });
}
export function imExcel<T, P>(url: string, data?: T): Promise<Result<P>> {
  return http.request<any>(
    "post",
    url,
    { data },
    // 自定义的axios配置在下面对象填写即可
    {
      headers: {
        "Content-Type": "multipart/form-data;"
      }
    }
  );
}
