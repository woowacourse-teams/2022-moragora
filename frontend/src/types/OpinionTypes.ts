export type Opinion = {
  id: number;
  discussionId: number;
  content: string;
  createdAt: number;
  updatedAt: number | null;
};

export type OpinionRequestBody = {
  content: Opinion['content'];
};
