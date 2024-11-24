import clsx from 'clsx';
import {Category, SocialService} from '@/app/lib/definitions';
import Link from "next/link";
import {fetchSocialServices} from "@/app/lib/data";
export default async function SocialServices() {

    const socialservices = await fetchSocialServices();

    function getServiceLink(socialService: SocialService) {
        if (socialService.website != null) {
            return (
                <p className="text-sm text-gray-500 sm:block">
                    <Link href={socialService.website} target="_blank">Zum Angebot</Link>
                </p>
            )
        }
        return ``;
    }

    function showCategories(socialService: SocialService) {
        return (
          <ul>
              {
                  socialService.categories.map((entry: Category) => {
                     return (<li key={entry.id} className="text-sm font-semibold">{entry.name}</li>);
                  })
              }
          </ul>
        );
    }

    function getListOfServices(socialServices: SocialService[]) {
        return <>
            {socialServices.map((socialservice, i) => {
                return (
                    <div
                        key={socialservice.id}
                        className={clsx(
                            'flex flex-row items-center justify-between py-4 md:grid-cols-4',
                            {
                                'border-t': i !== 0,
                            },
                        )}
                    >
                        <div className="md:grid-cols-2">
                            <p className="truncate text-sm font-semibold md:text-base">
                                {socialservice.name}
                            </p>
                            <p className="truncate md:text-base">
                                {socialservice.address}, {socialservice.postCode} {socialservice.city}
                            </p>
                            {getServiceLink(socialservice)}
                        </div>
                        <div className="md:grid-cols-2">
                            {showCategories(socialservice)}
                        </div>
                    </div>
                );
            })}
        </>;
    }

        return (
        <div className="flex w-full flex-col md:col-span-8">
            <div className="flex grow flex-col justify-between rounded-xl bg-gray-50 p-4">
                <div className="bg-white px-6">
                    {getListOfServices(socialservices)}
                </div>
            </div>
        </div>
    );
}
