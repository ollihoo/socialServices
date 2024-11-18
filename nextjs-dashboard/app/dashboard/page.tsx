import { lusitana } from '@/app/ui/fonts';
import SocialServices from "@/app/ui/dashboard/socialservices";
import {Suspense} from "react";
import {RevenueChartSkeleton} from "@/app/ui/skeletons";

export default async function Page() {
    return (
        <main>
            <h1 className={`${lusitana.className} antialiased mb-4 text-xl md:text-2xl`}>
                Dashboard
            </h1>
            <div className="mt-6 grid grid-cols-1 gap-6 md:grid-cols-4 lg:grid-cols-8">
                <Suspense fallback={<RevenueChartSkeleton />}>
                    <SocialServices />
                </Suspense>
            </div>
        </main>
    );
}