
#include <stdio.h>
#include <time.h>
int main(int argc, char const *argv[])
{
	/* code */
    int simetri;
    if(argc > 1) simetri=-1;
    else simetri=1;
	int n;
    printf("%d\n",simetri);
	scanf("%d",&n);
	int A[n][n];
	int temp,j,i;
	srand (time(NULL)); 
	for( i = 0; i < n;i++)
	{
		for( j = i; j < n; j++)
		{
		temp=rand()%2;
		A[i][j] =  temp;
		A[j][i] = temp*simetri;
		}
	}

	   
	for( i = 0; i < n;i++)
	{
		
		for( j = 0; j < n; j++)
		{
		printf("%d ", A[i][j] );
		
		}
		printf("\n");
	}
 
	return 0;
}
