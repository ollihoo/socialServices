export const dynamic = 'force-static'

export async function GET() {
    const res = await fetch('http://localhost:8080/social', {
        headers: {
            'Content-Type': 'application/json',
        },
    })
    const data = await res.json()
    return data;
}