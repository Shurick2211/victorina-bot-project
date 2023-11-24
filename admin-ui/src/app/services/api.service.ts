import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Victorina} from "../dto/victorina";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private api = 'http://127.0.0.1:5000/victorinas';

  constructor(private http: HttpClient) { }

  createVictorina(victorina:Victorina):Observable<any>{
    return this.http.post<any>(this.api,victorina,{observe:'response'})
  }

  getAllVictorinas():Observable<Victorina[]>{
    return this.http.get<Victorina[]>(this.api)
  }

  deleteVictorina(id: String):Observable<any>{
    return this.http.delete<any>(this.api,{body: id, observe:'response'})
  }
}
