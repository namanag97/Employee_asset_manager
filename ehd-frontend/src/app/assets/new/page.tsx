'use client';

import { useRouter } from 'next/navigation';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import AppLayout from '@/components/layout/AppLayout';
import AssetForm from '@/components/assets/AssetForm';
import { createAsset, fetchHardwareTypes } from '@/lib/api/assets';

export default function NewAssetPage() {
  const router = useRouter();
  const queryClient = useQueryClient();

  // Fetch hardware types
  const { data: hardwareTypes, isLoading: isLoadingTypes } = useQuery({
    queryKey: ['hardware-types'],
    queryFn: fetchHardwareTypes,
  });

  // Create asset mutation
  const { mutate, isPending, error } = useMutation({
    mutationFn: createAsset,
    onSuccess: () => {
      // Invalidate the assets query to refetch the list
      queryClient.invalidateQueries({ queryKey: ['assets'] });
      // Redirect to the assets list
      router.push('/assets');
    },
  });

  if (isLoadingTypes) {
    return (
      <AppLayout>
        <div className="text-center">
          <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-r-transparent align-[-0.125em] motion-reduce:animate-[spin_1.5s_linear_infinite]" />
          <p className="mt-4 text-sm text-gray-500">Loading hardware types...</p>
        </div>
      </AppLayout>
    );
  }

  return (
    <AppLayout>
      <div className="sm:flex sm:items-center">
        <div className="sm:flex-auto">
          <h1 className="text-2xl font-semibold text-gray-900">Add New Asset</h1>
          <p className="mt-2 text-sm text-gray-700">
            Add a new hardware asset to the system.
          </p>
        </div>
      </div>

      {hardwareTypes && (
        <div className="mt-8">
          <AssetForm
            hardwareTypes={hardwareTypes}
            onSubmit={mutate}
            isLoading={isPending}
            error={error ? 'Failed to create asset' : null}
          />
        </div>
      )}
    </AppLayout>
  );
} 