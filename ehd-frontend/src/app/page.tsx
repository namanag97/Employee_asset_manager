'use client';

import { useQuery } from '@tanstack/react-query';
import { fetchAssets } from '@/lib/api/assets';
import { fetchEmployees } from '@/lib/api/employees';

export default function HomePage() {
  const { data: assets, isLoading: isLoadingAssets } = useQuery({
    queryKey: ['assets'],
    queryFn: fetchAssets,
  });

  const { data: employees, isLoading: isLoadingEmployees } = useQuery({
    queryKey: ['employees'],
    queryFn: fetchEmployees,
  });

  const isLoading = isLoadingAssets || isLoadingEmployees;

  return (
    <>
      <div className="sm:flex sm:items-center">
        <div className="sm:flex-auto">
          <h1 className="text-2xl font-semibold text-gray-900">EHD Dashboard</h1>
          <p className="mt-2 text-sm text-gray-700">
            Overview of hardware assets and employees.
          </p>
        </div>
      </div>

      {isLoading ? (
        <div className="mt-8 text-center">
          <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-r-transparent align-[-0.125em] motion-reduce:animate-[spin_1.5s_linear_infinite]" />
          <p className="mt-4 text-sm text-gray-500">Loading dashboard data...</p>
        </div>
      ) : (
        <div className="mt-8 grid grid-cols-1 gap-6 sm:grid-cols-2">
          <div className="rounded-lg bg-white p-6 shadow">
            <h2 className="text-lg font-medium text-gray-900">Hardware Assets</h2>
            <p className="mt-2 text-3xl font-semibold text-gray-900">{assets?.length || 0}</p>
            <p className="mt-2 text-sm text-gray-500">Total assets in the system</p>
          </div>
          
          <div className="rounded-lg bg-white p-6 shadow">
            <h2 className="text-lg font-medium text-gray-900">Employees</h2>
            <p className="mt-2 text-3xl font-semibold text-gray-900">{employees?.length || 0}</p>
            <p className="mt-2 text-sm text-gray-500">Total employees in the system</p>
          </div>
        </div>
      )}
    </>
  );
}
