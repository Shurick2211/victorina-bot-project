import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
import {Victorina} from "../dto/victorina";
import {Observable} from "rxjs";
import {Person} from "../dto/person";
import {Channel} from "../dto/channel";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  //private url = 'http://victorina.eu-central-1.elasticbeanstalk.com'
  private url = ''
  private apiVictorina = `${this.url}/victorinas`;
  private apiPerson = `${this.url}/persons`;
  private apiOthers = `${this.url}/others`;

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

  savePerson(id:string, person:Person):Observable<any>{
    let header = new HttpHeaders().set("id", id)
    return this.http.put<any>(this.apiPerson,person,{observe:'response', headers:header})
  }

  sendAdminMess(mess:string, headerId:string, paramId:string | null){
    let paramMess = paramId !== null ? new HttpParams().set("mess", mess).set("receiverId", paramId) : new HttpParams().set("mess", mess)
    let header = new HttpHeaders().set("id", headerId)
    this.http.get<any>(this.apiOthers +'/message',{params:paramMess, observe:"response", headers:header})
      .subscribe(response =>
        console.log(response.status))
  }

  getPersons(page:number, perPage:number,  headId:string):Observable<HttpResponse<Person[]>>{
    let header = new HttpHeaders().set("id", headId)
    let param = new HttpParams().set("perpage", perPage).set("page", page)
    return this.http.get<Person[]>(this.apiPerson,{observe:'response', headers:header, params:param})
  }

  getChannels(headId:string):Observable<HttpResponse<Channel[]>>{
    let header = new HttpHeaders().set("id", headId)
    return this.http.get<Channel[]>(this.apiOthers + "/channels",{observe:'response', headers:header})
  }

}
