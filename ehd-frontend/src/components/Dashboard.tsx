import { Suspense } from 'react';
import Link from 'next/link';
import AssetList from './assets/AssetList';
import EmployeeList from './employees/EmployeeList';
import LoadingSpinner from './LoadingSpinner';

export default function Dashboard() {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
      <section>
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-semibold">Hardware Assets</h2>
          <Link
            href="/assets/new"
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Add Asset
          </Link>
        </div>
        <Suspense fallback={<LoadingSpinner />}>
          <AssetList />
        </Suspense>
      </section>

      <section>
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-semibold">Employees</h2>
          <Link
            href="/employees/new"
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Add Employee
          </Link>
        </div>
        <Suspense fallback={<LoadingSpinner />}>
          <EmployeeList />
        </Suspense>
      </section>
    </div>
  );
} 