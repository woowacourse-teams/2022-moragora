export type Discussion = {
  id: number;
  title: string;
  content: string;
  views: number;
  createdAt: number;
  updatedAt: number | null;
};

export type DiscussionsRequestBody = {
  title: Discussion['title'];
  content: Discussion['content'];
};
