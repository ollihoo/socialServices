import {
  Revenue,
  SocialService
} from './definitions';

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
    const res = await fetch('http://localhost:8080/social', {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const data = await res.json();
    const resultSocialServices: SocialService[] = data.map((item:any) => {
      return {
        id: item.id,
        name: item.name,
        address: item.address,
        postCode: item.postCode,
        city: item.city,
        website: item.website,
        categories:item.categories,
      };
    });
    return resultSocialServices;
}
