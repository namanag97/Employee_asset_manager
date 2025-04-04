'use client';

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import AppLayout from '@/components/layout/AppLayout';
import EmployeeForm from '@/components/employees/EmployeeForm';
import { fetchEmployee, fetchEmployeeAssets, updateEmployee } from '@/lib/api/employees';
import type { Employee } from '@/types';

export default function EmployeeDetailPage({ params }: { params: { id: string } }) {
  const queryClient = useQueryClient();

  const { data: employee, isLoading: isLoadingEmployee } = useQuery({
    queryKey: ['employee', params.id],
    queryFn: () => fetchEmployee(params.id),
  });

  const { data: assignedAssets = [], isLoading: isLoadingAssets } = useQuery({
    queryKey: ['employee-assets', params.id],
    queryFn: () => fetchEmployeeAssets(params.id),
  });

  const updateMutation = useMutation({
    mutationFn: (data: Partial<Employee>) => updateEmployee(params.id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['employee', params.id] });
      queryClient.invalidateQueries({ queryKey: ['employees'] });
    },
  });

  if (isLoadingEmployee) {
    return (
      <AppLayout>
        <div className="text-center">
          <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-r-transparent align-[-0.125em] motion-reduce:animate-[spin_1.5s_linear_infinite]" />
          <p className="mt-4 text-sm text-gray-500">Loading employee details...</p>
        </div>
      </AppLayout>
    );
  }

  if (!employee) {
    return (
      <AppLayout>
        <div className="text-center">
          <h1 className="text-2xl font-semibold text-gray-900">Employee not found</h1>
          <p className="mt-2 text-sm text-gray-500">The requested employee could not be found.</p>
        </div>
      </AppLayout>
    );
  }

  return (
    <AppLayout>
      <div className="sm:flex sm:items-center">
        <div className="sm:flex-auto">
          <h1 className="text-2xl font-semibold text-gray-900">Employee Details</h1>
          <p className="mt-2 text-sm text-gray-700">
            View and manage employee information.
          </p>
        </div>
      </div>

      <div className="mt-8">
        <EmployeeForm
          employee={employee}
          onSubmit={updateMutation.mutate}
          isLoading={updateMutation.isPending}
          error={updateMutation.error ? 'Failed to update employee' : null}
        />
      </div>

      <div className="mt-8">
        <h2 className="text-lg font-medium text-gray-900">Assigned Assets</h2>
        {isLoadingAssets ? (
          <div className="mt-4 text-center">
            <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-r-transparent align-[-0.125em] motion-reduce:animate-[spin_1.5s_linear_infinite]" />
            <p className="mt-2 text-sm text-gray-500">Loading assigned assets...</p>
          </div>
        ) : assignedAssets.length === 0 ? (
          <p className="mt-4 text-sm text-gray-500">No assets assigned to this employee.</p>
        ) : (
          <div className="mt-4 overflow-hidden shadow ring-1 ring-black ring-opacity-5 sm:rounded-lg">
            <table className="min-w-full divide-y divide-gray-300">
              <thead className="bg-gray-50">
                <tr>
                  <th scope="col" className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-6">
                    Serial Number
                  </th>
                  <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
                    Model
                  </th>
                  <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
                    Manufacturer
                  </th>
                  <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
                    Status
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200 bg-white">
                {assignedAssets.map((asset) => (
                  <tr key={asset.id}>
                    <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6">
                      {asset.serialNumber}
                    </td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                      {asset.model}
                    </td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                      {asset.manufacturer}
                    </td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                      {asset.status}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </AppLayout>
  );
} 