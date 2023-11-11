import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Victorina} from "../dto/victorina";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private api = 'frontrequests';

  constructor(private http: HttpClient) { }

  createVictorina(victorina:Victorina):Observable<any>{
    return this.http.post<any>(this.api,victorina,{observe:'response'})
  }
}
