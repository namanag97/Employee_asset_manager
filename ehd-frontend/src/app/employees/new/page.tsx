'use client';

import { useRouter } from 'next/navigation';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import AppLayout from '@/components/layout/AppLayout';
import EmployeeForm from '@/components/employees/EmployeeForm';
import { createEmployee } from '@/lib/api/employees';
import type { Employee } from '@/types';

export default function NewEmployeePage() {
  const router = useRouter();
  const queryClient = useQueryClient();

  const createMutation = useMutation({
    mutationFn: createEmployee,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['employees'] });
      router.push('/employees');
    },
  });

  return (
    <AppLayout>
      <div className="sm:flex sm:items-center">
        <div className="sm:flex-auto">
          <h1 className="text-2xl font-semibold text-gray-900">Add New Employee</h1>
          <p className="mt-2 text-sm text-gray-700">
            Add a new employee to the system.
          </p>
        </div>
      </div>

      <div className="mt-8">
        <EmployeeForm
          onSubmit={createMutation.mutate}
          isLoading={createMutation.isPending}
          error={createMutation.error ? 'Failed to create employee' : null}
        />
      </div>
    </AppLayout>
  );
} 