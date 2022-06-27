export interface IDealer {
    id?: number;
    name?: string; 
    city?: string;
    address?: string; 
    phone?: string;
  }
  
  export class Dealer implements IDealer {
    constructor(
        public id: number, 
        public name: string, 
        public city: string, 
        public address: string, 
        public phone: string
        ) {}
  }
  
  export function getDealerIdentifier(dealer: IDealer): number | undefined {
    return dealer.id;
  }
  