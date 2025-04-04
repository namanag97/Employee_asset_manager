'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import AppLayout from '@/components/layout/AppLayout';
import AssignAssetModal from '@/components/assets/AssignAssetModal';
import ReturnAssetModal from '@/components/assets/ReturnAssetModal';
import { fetchAsset, assignAsset, returnAsset } from '@/lib/api/assets';
import type { HardwareStatus } from '@/types';

export default function AssetDetailPage({ params }: { params: { id: string } }) {
  const queryClient = useQueryClient();
  const [isAssignModalOpen, setIsAssignModalOpen] = useState(false);
  const [isReturnModalOpen, setIsReturnModalOpen] = useState(false);

  const { data: asset, isLoading } = useQuery({
    queryKey: ['asset', params.id],
    queryFn: () => fetchAsset(params.id),
  });

  const assignMutation = useMutation({
    mutationFn: ({ assetId, employeeId }: { assetId: string; employeeId: string }) =>
      assignAsset(assetId, employeeId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['asset', params.id] });
      queryClient.invalidateQueries({ queryKey: ['assets'] });
      setIsAssignModalOpen(false);
    },
  });

  const returnMutation = useMutation({
    mutationFn: ({ assetId, status }: { assetId: string; status: HardwareStatus }) =>
      returnAsset(assetId, status),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['asset', params.id] });
      queryClient.invalidateQueries({ queryKey: ['assets'] });
      setIsReturnModalOpen(false);
    },
  });

  if (isLoading) {
    return (
      <AppLayout>
        <div className="text-center">
          <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-r-transparent align-[-0.125em] motion-reduce:animate-[spin_1.5s_linear_infinite]" />
          <p className="mt-4 text-sm text-gray-500">Loading asset details...</p>
        </div>
      </AppLayout>
    );
  }

  if (!asset) {
    return (
      <AppLayout>
        <div className="text-center">
          <h1 className="text-2xl font-semibold text-gray-900">Asset not found</h1>
          <p className="mt-2 text-sm text-gray-500">The requested asset could not be found.</p>
        </div>
      </AppLayout>
    );
  }

  return (
    <AppLayout>
      <div className="sm:flex sm:items-center">
        <div className="sm:flex-auto">
          <h1 className="text-2xl font-semibold text-gray-900">Asset Details</h1>
          <p className="mt-2 text-sm text-gray-700">
            View and manage asset information.
          </p>
        </div>
        <div className="mt-4 sm:ml-16 sm:mt-0 sm:flex-none">
          {asset.status === 'Available' && (
            <button
              type="button"
              onClick={() => setIsAssignModalOpen(true)}
              className="block rounded-md bg-indigo-600 px-3 py-2 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              Assign Asset
            </button>
          )}
          {asset.status === 'Assigned' && (
            <button
              type="button"
              onClick={() => setIsReturnModalOpen(true)}
              className="block rounded-md bg-indigo-600 px-3 py-2 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              Return Asset
            </button>
          )}
        </div>
      </div>

      <div className="mt-8 flow-root">
        <div className="-mx-4 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
          <div className="inline-block min-w-full py-2 align-middle sm:px-6 lg:px-8">
            <div className="overflow-hidden shadow ring-1 ring-black ring-opacity-5 sm:rounded-lg">
              <table className="min-w-full divide-y divide-gray-300">
                <tbody className="divide-y divide-gray-200 bg-white">
                  <tr>
                    <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6">
                      Serial Number
                    </td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                      {asset.serialNumber}
                    </td>
                  </tr>
                  <tr>
                    <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6">
                      Model
                    </td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                      {asset.model}
                    </td>
                  </tr>
                  <tr>
                    <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6">
                      Manufacturer
                    </td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                      {asset.manufacturer}
                    </td>
                  </tr>
                  <tr>
                    <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6">
                      Status
                    </td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                      {asset.status}
                    </td>
                  </tr>
                  {asset.assignedTo && (
                    <tr>
                      <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6">
                        Assigned To
                      </td>
                      <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                        {asset.assignedTo.firstName} {asset.assignedTo.lastName}
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <AssignAssetModal
        isOpen={isAssignModalOpen}
        onClose={() => setIsAssignModalOpen(false)}
        assetId={params.id}
        onAssign={(employeeId) => assignMutation.mutate({ assetId: params.id, employeeId })}
        isLoading={assignMutation.isPending}
      />

      <ReturnAssetModal
        isOpen={isReturnModalOpen}
        onClose={() => setIsReturnModalOpen(false)}
        assetId={params.id}
        onReturn={(status) => returnMutation.mutate({ assetId: params.id, status })}
        isLoading={returnMutation.isPending}
      />
    </AppLayout>
  );
} 