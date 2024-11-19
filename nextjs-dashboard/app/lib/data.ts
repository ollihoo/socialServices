import {
  InvoicesTable,
  Revenue,
  SocialService
} from './definitions';

export function fetchFilteredInvoices(query: string, currentPage: number) {
  console.log("Query: "+query+"; currentPage: "+ currentPage);
  const data : InvoicesTable[] = [];
  data.push({
    id: "aaaaaaaaaa",
    name: "Mr. Smith",
    image_url: "/wespe.jpg",
    email: "alan@smith.org",
    amount: 240000,
    customer_id: "werewww",
    status: 'pending',
    date: "2024-11-12"
  });
  return data;
}

export async function fetchInvoicesPages(query: string) {
  // another request for the pagination to the database?? ;-(
  // simulating, that it's two pages...
  return 2;
}

export async function fetchRevenue() {
  try {
    const data : Revenue[] = [];
    data.push({month: "Jan" , revenue: 23 });
    data.push({month: "Feb" , revenue: 323 });
    data.push({month: "Mar" , revenue: 523 });
    data.push({month: "Apr" , revenue: 723 });
    data.push({month: "Jun" , revenue: 623 });
    data.push({month: "Jul" , revenue: 823 });
    data.push({month: "Aug" , revenue: 423 });
    data.push({month: "Sep" , revenue: 823 });
    data.push({month: "Oct" , revenue: 923 });
    data.push({month: "Nov" , revenue: 723 });
    data.push({month: "Dec" , revenue: 1223 });
    return data;
  } catch (error) {
    console.error('Database Error:', error);
    throw new Error('Failed to fetch revenue data.');
  }
}

export async function fetchSocialServices() {
  function createSocialServices(input: any) {
    const resultSocialServices: SocialService[] = input.map((item: any) => {
      return {
        id: item.id,
        name: item.name,
        address: item.address,
        postCode: item.postCode,
        city: item.city,
        website: item.website,
        categories: item.categories,
      };
    });
    return resultSocialServices;
  }

  let res;
    try {
       res = await fetch('http://localhost:8080/social', {
        headers: {
          'Content-Type': 'application/json',
        },
      });
    } catch (error) {
      console.log("SocialServices problem: ${error.message}");
      return [];
    }
    const data = await res.json();
    return createSocialServices(data);
}
