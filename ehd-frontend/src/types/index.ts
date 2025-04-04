// Hardware Asset Types
export type HardwareStatus = 'Available' | 'Assigned' | 'Maintenance' | 'Retired';

export interface HardwareAsset {
  assetId: number;
  assetTag: string;
  serialNumber: string;
  typeId: number;
  make: string;
  model: string;
  specifications?: string;
  status: HardwareStatus;
  notes?: string;
  currentEmployeeId?: string;
  lastAssignmentDate?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateAssetRequest {
  assetTag: string;
  serialNumber: string;
  typeId: number;
  make: string;
  model: string;
  specifications?: string;
  status: string;
  notes?: string;
}

// Hardware Type Types
export interface HardwareType {
  typeId: number;
  typeName: string;
  description?: string;
  createdAt: string;
  updatedAt: string;
}

// Employee Types
export interface Employee {
  employeeId: string;
  fullName: string;
  email: string;
  status: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateEmployeeRequest {
  employeeId: string;
  fullName: string;
  email: string;
  status: string;
}

// Assignment History Types
export interface AssignmentHistory {
  historyId: number;
  assetId: number;
  employeeId: string;
  employeeName: string;
  assignmentDate: string;
  returnDate?: string;
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface AssetHistory extends AssignmentHistory {
  status: HardwareStatus;
  action: 'ASSIGN' | 'RETURN' | 'UPDATE';
  previousStatus?: HardwareStatus;
  newStatus?: HardwareStatus;
}

// API Response Types
export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface PaginatedResponse<T> {
  data: T[];
  total: number;
  page: number;
  limit: number;
  totalPages: number;
} 