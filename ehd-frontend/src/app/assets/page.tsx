'use client';

import { useQuery } from '@tanstack/react-query';
import AppLayout from '@/components/layout/AppLayout';
import AssetTable from '@/components/assets/AssetTable';
import { fetchAssets } from '@/lib/api/assets';

export default function AssetsPage() {
  const { data: assets, isLoading, error } = useQuery({
    queryKey: ['assets'],
    queryFn: fetchAssets,
  });

  return (
    <AppLayout>
      <div className="sm:flex sm:items-center">
        <div className="sm:flex-auto">
          <h1 className="text-2xl font-semibold text-gray-900">Hardware Assets</h1>
          <p className="mt-2 text-sm text-gray-700">
            A list of all hardware assets in the system.
          </p>
        </div>
      </div>

      {isLoading ? (
        <div className="mt-8 text-center">
          <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-r-transparent align-[-0.125em] motion-reduce:animate-[spin_1.5s_linear_infinite]" />
          <p className="mt-4 text-sm text-gray-500">Loading assets...</p>
        </div>
      ) : error ? (
        <div className="mt-8 rounded-md bg-red-50 p-4">
          <div className="flex">
            <div className="ml-3">
              <h3 className="text-sm font-medium text-red-800">Error loading assets</h3>
              <div className="mt-2 text-sm text-red-700">
                <p>{(error as Error).message}</p>
              </div>
            </div>
          </div>
        </div>
      ) : assets ? (
        <AssetTable assets={assets} />
      ) : null}
    </AppLayout>
  );
} 