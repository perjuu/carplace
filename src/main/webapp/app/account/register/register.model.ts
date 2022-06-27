import { Dealer } from "app/entities/dealer/dealer.model";

export class Registration {
  constructor(
    public login: string,
    public email: string,
    // public dealerInfo: {name:string, city: string, address: string, phone: string },
    public name: Dealer,
    public city: Dealer,
    public address: Dealer,
    public phone: Dealer,
    public password: string,
    public langKey: string
    ) {}
}
