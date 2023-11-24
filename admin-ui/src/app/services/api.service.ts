import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {Victorina} from "../dto/victorina";
import {Observable} from "rxjs";
import {Person} from "../dto/person";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiVictorina = 'http://127.0.0.1:5000/victorinas';
  private apiPerson = 'http://127.0.0.1:5000/persons';

  constructor(private http: HttpClient) { }

  createVictorina(victorina:Victorina):Observable<any>{
    return this.http.post<any>(this.apiVictorina,victorina,{observe:'response'})
  }

  getAllVictorinas():Observable<Victorina[]>{
    return this.http.get<Victorina[]>(this.apiVictorina)
  }

  deleteVictorina(id: String):Observable<any>{
    return this.http.delete<any>(this.apiVictorina,{body: id, observe:'response'})
  }

  getPerson(id:string):Observable<HttpResponse<Person>>{
    return this.http.post<Person>(this.apiPerson,id,{observe:'response'})
  }

  savePassFromPerson(id:string, pass:String):Observable<any>{
    let param = new HttpParams()
    param.append("id", id)
    return this.http.post<any>(this.apiPerson,pass,{observe:'response', params:param})
  }
}
