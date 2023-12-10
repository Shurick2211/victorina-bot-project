import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
import {Victorina} from "../dto/victorina";
import {Observable} from "rxjs";
import {Person} from "../dto/person";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  //private url = 'http://victorina.eu-central-1.elasticbeanstalk.com'
  private url = ''
  private apiVictorina = `${this.url}/victorinas`;
  private apiPerson = `${this.url}/persons`;

  constructor(private http: HttpClient) { }

  createVictorina(victorina:Victorina):Observable<any>{
    return this.http.post<any>(this.apiVictorina,victorina,{observe:'response'})
  }

  getAllVictorinas():Observable<Victorina[]>{
    return this.http.get<Victorina[]>(this.apiVictorina)
  }

  getAllUserVictorinas(id:string):Observable<Victorina[]>{
    let param = new HttpParams().set("id", id)
    return this.http.get<Victorina[]>(this.apiVictorina,{params:param})
  }

  deleteVictorina(id: String):Observable<any>{
    return this.http.delete<any>(this.apiVictorina,{body: id, observe:'response'})
  }

  getPerson(id:string, headId:string):Observable<HttpResponse<Person>>{
    let header = new HttpHeaders().set("id", headId)
    return this.http.post<Person>(this.apiPerson,id,{observe:'response', headers:header})
  }

  savePassFromPerson(id:string, pass:String):Observable<any>{
    let header = new HttpHeaders().set("id", id)
    return this.http.put<any>(this.apiPerson,pass,{observe:'response', headers:header})
  }

  sendAdminMess(mess:string, userId:string){
    let paramMess = new HttpParams().set("mess", mess)
    let header = new HttpHeaders().set("id", userId)
    this.http.get<any>(this.apiPerson+'/message',{params:paramMess, observe:"response", headers:header})
      .subscribe(response =>
        console.log(response.status))
  }

}
