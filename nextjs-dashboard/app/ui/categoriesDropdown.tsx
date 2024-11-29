"use client";
import {Category} from "@/app/lib/definitions";
import {usePathname, useRouter, useSearchParams} from "next/navigation";

export default function CategoriesDropdown({ categories, placeholder }: { categories: Category[], placeholder: string }) {
  const selectedOption = "";
  const searchParams = useSearchParams();
  const pathName = usePathname();
  const { replace } = useRouter();

  const handleSelection = (selectionEvent: any) => {
    console.log("Selection is " + selectionEvent);
    const params = new URLSearchParams(searchParams);
  };

  return (
    <div className="relative flex flex-1 flex-shrink-0">
      <div>
        <label htmlFor="categoryDropdown" className="sr-only">Choose a category;</label>
        <select id="categoryDropdown" value={selectedOption} onChange={handleSelection}>
          <option value="" disabled>Select a category</option>
          {
            categories.map((category: Category) => {
              return (<option key={category.id} value={category.id}>{category.name}</option>);
            })
          }
        </select>
      </div>
    </div>
  );
}
